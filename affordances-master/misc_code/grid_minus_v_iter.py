import copy
import random
import sys
from itertools import chain


# Globals

if len(sys.argv) == 1 or sys.argv[1] == "1":
	a = -0.04

elif sys.argv[1] == "2":
	a = 0.03

stateSpace = [[-0.04, a, -0.04],
				  [-0.04, 0, -0.04],
	  			  [-0.04, -0.04, -0.04],
				  [-0.04, -1, 1]]


botLoc = (0,0)

def god_transition(botLoc, stateSpace, action):
	# Stores the laws of the world (actual T)
	actionProbs = {action : 0.8}

	if botLoc[0] == action[0]:
		# Y diff
		actionProbs.update({(botLoc[0] + 1, botLoc[1]) : 0.1, (botLoc[0] - 1, botLoc[1]) : 0.1})

		# Check to see if both directions legit

		if botLoc[0] + 1 >= len(stateSpace) or stateSpace[botLoc[0] + 1][botLoc[1]] is None:
			del actionProbs[(botLoc[0] + 1, botLoc[1])]
			actionProbs[action] += 0.1
		
		if action[0] - 1 < 0 or stateSpace[botLoc[0] - 1][botLoc[1]] is None:
			del actionProbs[(botLoc[0] - 1, botLoc[1])]
			actionProbs[action] += 0.1

	elif botLoc[1] == action[1]:
		# X diff
		actionProbs.update({(botLoc[0], botLoc[1] + 1) : 0.1, (botLoc[0], botLoc[1] - 1) : 0.1})

		# Check to see if both directions legit
		if botLoc[1] + 1 >= len(stateSpace[0]) or stateSpace[botLoc[0]][botLoc[1] + 1] is None:
			del actionProbs[(botLoc[0], botLoc[1] + 1)]
			actionProbs[action] += 0.1

		if botLoc[1] - 1 < 0 or stateSpace[botLoc[0]][botLoc[1] - 1] is None:
			del actionProbs[(botLoc[0], botLoc[1] - 1)]
			actionProbs[action] += 0.1
	else:
		raise AttributeError("Bad transition")

	return actionProbs

def update_bot_transition(bot_transition, botLoc, stateSpace, action):
# bot_transition: Dictionary where keys are state-action pairs, and the value is a 4x3 matrix containing the probability of that state-action pair landing you in each state
	pass

def actions(loc, stateSpace):
	# Returns legitimate actions for the agent acting in 'loc'
	_actions = []
	for i in [-1,1]:
		# Check y
		if 0 <= loc[0] + i < len(stateSpace) and stateSpace[loc[0] + i][loc[1]]:
			_actions.append((loc[0] + i, loc[1]))

		if 0 <=	 loc[1] + i < len(stateSpace[0]) and stateSpace[loc[0]][loc[1] + i]:
			_actions.append((loc[0], loc[1] + i))

	return _actions

def value_iteration(epsilon, discount, botTransition):
	utility = [[0, 0, 0],
			[0, 0, 0],
			[0, 0, 0],
			[0, -1, 1]]

	delta = 999999999999
	k = 0
	while delta > epsilon * (1 - discount) / discount:
		delta = 0
		k += 1
		for i in range(len(stateSpace)):
			for j in range(len(stateSpace[0])):
				if stateSpace[i][j] is None or (i == 3 and (j == 1 or j == 2)):
					continue
				
				expUtilActs = []
				# Loop over possible (legit) actions and find highest expected utility
				for a in actions((i,j),stateSpace):
					#aProbs = god_transition((i,j), stateSpace, a)
					# aProbs = botTransition((i,j), stateSpace, a)

					# Get transition counts and normalize to get probabilities
					aProbs = botTransition[((i,j), a)]
					aProbs = {k: float(v) / sum(aProbs.values()) for k, v in aProbs.iteritems()}

					# Calculate Expected Utility of each action
					EU = 0
					for newState in aProbs:
						newStateProb = aProbs[newState]
						EU += newStateProb * utility[newState[0]][newState[1]]

					expUtilActs.append(EU)

				# Update U
				prevUtil = utility[i][j]
				utility[i][j] = stateSpace[i][j] + discount * max(expUtilActs)
				if abs(utility[i][j] - prevUtil) > delta:
					delta = abs(utility[i][j] - prevUtil)
		if k > 1000:
			print "BROKE FROM K"
			break

	return utility

def learn(stateSpace, botTransition, curLoc, n):
# Update botTransition via random exploration of magnitude n
	for i in range(n):
		a = actions(curLoc, stateSpace)
		randAct = random.choice(a)
		botTransition[(curLoc, randAct)][randAct] +=1
		curLoc = randAct

	return botTransition

def exec_policy(utility, curLoc, stateSpace, timeThreshold = 300):
	# Given a utility matrix containing the U of all states, executes a 
	# policy and displays the state space along the way
	
	done = False
	count = 0
	while not done:
		count += 1
		display_utility_space(utility, curLoc)
		a = actions(curLoc, stateSpace)
		random.shuffle(a)
		bestAction = max([(i, j) for i, j in a], key=lambda x: utility[x[0]][x[1]])
		curLoc = bestAction

		if _is_goal(curLoc):
			done = True
			display_utility_space(utility, curLoc)
			return count

		if count > timeThreshold:
			return count

def _is_goal(loc):
	if loc == (3,2):
		return True
	else:
		return False

def display_state_space(stateSpace, botLoc):
	# Prints out the state space
	print "\n" + "="*21
	for row in range(4):
		for col in range(3):
			if botLoc == (row,col):
				print "b",
			else:
				print stateSpace[row][col],
			print "\t",
		print
	print "="*21 + "\n"

def display_utility_space(utility, botLoc):
	# Prints out the state space
	print "\n" + "="*21
	for row in range(4):
		for col in range(3):
			if botLoc == (row,col):
				print "b",
			else:
				print "%.2f" % utility[row][col],
			print "\t",
		print
	print "="*21 + "\n"

def main():
	# Builds bot transition model (T) - stores counts instead of probabilities
	botTransition = {}
	for row in range(4):
		for col in range(3):
			for a in actions((row, col), stateSpace):
				botTransition[((row, col), a)] = {(i, j): 1 for i in range(4) for j in range(3)}
	
	util = value_iteration(0.1, 0.95, botTransition)

	# Random Walk to try and maximize reward
	# print "Before learning T:"
	# counts = []
	# for i in range(10):
	# 	botLoc = (0, 0)
	# 	c = exec_policy(util, botLoc, stateSpace)
	# 	counts.append(c)
	# print sum(counts) / float(len(counts))
	# print max(counts)
	# print
	# print "Learning..."
	
	botTransition = learn(stateSpace, botTransition, (0,0), 99)
	util = value_iteration(0.1, 0.95, botTransition) 

	counts = []
	for i in range(10):
		botLoc = (0, 0)
		c = exec_policy(util, botLoc, stateSpace)
		counts.append(c)
	print sum(counts) / float(len(counts))
	print max(counts)
	print
	print botTransition

main()