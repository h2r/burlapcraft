import copy

stateSpace = [[-0.04, -0.04, -0.04],
			  [-0.04, None, -0.04],
  			  [-0.04, -0.04, -0.04],
			  [-0.04, -1, 1]]

botLoc = (0,0)

def transition(botLoc, stateSpace, action):

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


def actions(loc, stateSpace):
	_actions = []
	for i in [-1,1]:
		# Check y
		if 0 <= loc[0] + i < len(stateSpace) and stateSpace[loc[0] + i][loc[1]]:
			_actions.append((loc[0] + i, loc[1]))

		if 0 <=	 loc[1] + i < len(stateSpace[0]) and stateSpace[loc[0]][loc[1] + i]:
			_actions.append((loc[0], loc[1] + i))

	return _actions

def valueIteration(epsilon, discount):
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
					aProbs = transition((i,j), stateSpace, a)
					# print aProbs, (i, j)
					EU = 0
					for newState in aProbs:
						newStateProb = aProbs[newState]
						EU += newStateProb * utility[newState[0]][newState[1]]

					expUtilActs.append(EU)

				# print max(expUtilActs)
				# Update U
				prevUtil = utility[i][j]
				utility[i][j] = stateSpace[i][j] + discount * max(expUtilActs)
				if abs(utility[i][j] - prevUtil) > delta:
					delta = abs(utility[i][j] - prevUtil)
		if k > 1000:
			print "BROKE FROM K"
			break

		print delta, epsilon * (1 - discount) / discount
	print utility
		# break 
	return utility
