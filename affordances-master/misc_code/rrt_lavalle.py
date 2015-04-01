# RRT (Rapidly Exploring Randomized Tree) | rrt_lavalle.py
# Gabriel Barth Maron & Dave Abel

import sys
import random as r
import math
import matplotlib.pyplot as pyplot

import pygame

fieldHeight = 400
fieldWidth = 400
SPEED = 5
K = 1000
deltaT = 1
pygame.init()
#create the screen
window = pygame.display.set_mode((420, 420))  

def generate_rrt(initState, K, deltaT):

	graph = {initState : set()}

	for i in range(K):

		# RANDOM STATE
		randState = (r.uniform(0, fieldWidth), r.uniform(0, fieldHeight))

		# NEAREST_NEIGHBOR
		nearestNeighbor = get_nearest_state(randState, graph)
		u =  (randState[0] - nearestNeighbor[0], randState[1] - nearestNeighbor[1])

		u = [(SPEED * j)/euc_dist(nearestNeighbor, randState) for j in u]
		# NEW_STATE
		newState = get_new_state(nearestNeighbor, u, deltaT)
		
		# Add vertex/edge of new state to graph
		graph[nearestNeighbor].add(newState)
		graph[newState] = set()
		draw_graph(graph)

	return graph

def get_nearest_state(state, g):
	minDist = sys.maxint
	nearestNeighbor = None
	for vertex in g:
		dist = euc_dist(vertex,state)
		if dist < minDist:
			minDist = dist
			nearestNeighbor = vertex

	return nearestNeighbor

def euc_dist(a,b):
	return ((a[0] - b[0])**2  + (a[1] - b[1])**2)**(0.5)

def get_new_state(nearestNeighbor, u, deltaT):
	return tuple(nearestNeighbor[i] + u[i] * deltaT for i in range(len(nearestNeighbor)))


def draw_graph(g):
	for vertex in g:
		# print g[vertex]
		pygame.draw.rect(window, (255,204,153), [vertex[0], vertex[1],2,2],1)
		for edgeDest in g[vertex]:
			pygame.draw.line(window, (123,204,255), vertex, edgeDest)

	#draw it to the screen
	pygame.display.flip() 

def main():
	graph = generate_rrt((fieldWidth/2, fieldHeight/2), K, deltaT)

if __name__ == '__main__':
	main()
