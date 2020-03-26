#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Mar 16 10:55:12 2020

@author: msi-gtfo
"""

import numpy as np
import matplotlib.pyplot as plt
import random

rewards1 = []
rewards2 = []

# Agent of player
Goal_reward = 100
Penalizer_reward = -10
Penalizer_movement = -1
Wall_reward = 0

UP = 0
DOWN = 1
LEFT = 2
RIGHT = 3

NUM_EPISODES = 70

'''
Matrix RF (States representation)
---------------------
| 0  | 1  | 2  | 3  |
---------------------
| 4  | X  | 6  | 7  |
---------------------
| 8  | 9  | 10 | 11 |
---------------------
'''

class Agent:

    def __init__(self, start=8, end=3, lr=0.2, gamma=0.96, exp_rate=0.3, epsilon=0.3, e_degradation=0.003):
        self.actions = ["up", "down", "left", "right"]
        self.start = start
        self.lr = lr
        self.gamma = gamma
        self.allrewards = []
        self.end = end
        self.Q_values = np.zeros((16,4))
        self.state = start
        self.epsilon=epsilon
        self.e_degradation = e_degradation

        self.reward = np.array([[Wall_reward, Penalizer_movement, Wall_reward, Penalizer_reward],       # position (0,0) - State 0
                   [Wall_reward, Wall_reward, Penalizer_movement, Penalizer_movement],                  # position (0,1) - State 1
                   [Wall_reward, Penalizer_movement, Penalizer_movement, Goal_reward],                  # position (0,2) - State 2
                   [Wall_reward, Penalizer_reward, Penalizer_movement, Wall_reward],                    # position (0,3) - State 3
                   
                   [Penalizer_movement, Penalizer_movement, Wall_reward, Wall_reward],                  # position (1,0) - State 4
                   [Wall_reward, Wall_reward, Wall_reward, Wall_reward],                                # position (1,1) - State 5
                   [Penalizer_movement, Penalizer_movement, Wall_reward, Penalizer_reward],             # position (1,2) - State 6
                   [Goal_reward, Penalizer_movement, Penalizer_reward, Wall_reward],                    # position (1,3) - State 7
                   
                   [Penalizer_movement, Wall_reward, Wall_reward, Penalizer_movement],                  # position (2,0) - State 8
                   [Wall_reward, Wall_reward, Penalizer_movement, Penalizer_movement],                  # position (2,1) - State 9
                   [Penalizer_movement, Wall_reward, Penalizer_movement, Penalizer_movement],           # position (2,2) - State 10
                   [Penalizer_reward, Wall_reward, Penalizer_movement, Wall_reward]])                   # position (2,3) - State 11
                   
        self.n_s = np.array([[-1,4,-1,1],   # position (0,0) - State 0
                    [-1,-1,0,2],            # position (1,0) - State 1
                    [-1,6,1,3],             # position (2,0) - State 2
                    [-1,7,2,-1],            # position (3,0) - State 3
                        
                    [0,8,-1,-1],            # position (1,0) - State 4
                    [1,9,4,6],              # position (1,1) - State 5
                    [2,10,-1,7],            # position (1,2) - State 6
                    [3,11,6,-1],            # position (1,3) - State 7
                        
                    [4,-1,-1,9],            # position (2,0) - State 8
                    [-1,-1,8,10],           # position (2,1) - State 9
                    [6,-1,9,11],            #  position (2,2) - State 10
                    [7,-1,10,-1]])          # position (2,3) - State 11
        
        self.actions = np.array([[DOWN,RIGHT],            # position (0,0) - State 0
                        [LEFT, RIGHT],      # position (0,1) - State 1
                        [DOWN, LEFT, RIGHT],      # position (0,2) - State 2
                        [DOWN, LEFT],             # position (0,3) - State 3
                        
                        [UP,DOWN],          # position (1,0) - State 4
                        [],    # position (1,1) - State 5
                        [UP,DOWN,RIGHT],    # position (1,2) - State 6
                        [UP,DOWN,LEFT],          # position (1,3) - State 7
                        
                        [UP,RIGHT],         # position (2,0) - State 8
                        [LEFT,RIGHT],    # position (2,1) - State 9
                        [UP,LEFT,RIGHT],    # position (2,2) - State 10
                        [UP,LEFT]])          # position (2,3) - State 11

    def play_q_learning(self, rounds=15):
        for _ in range(rounds):
            round_reward = 0
            self.state = self.start
            while self.state != self.end:
                Qx = -999
                for a in self.actions[self.state]:
                    if self.Q_values[self.state][a] > Qx:
                        Qx = self.Q_values[self.state][a]
                        act = a
                next_state = self.n_s[self.state][act]
                
                next_values = []
                for i in self.actions[next_state]:
                    next_values.append(self.Q_values[next_state][i])
                max_Q_value = max(next_values)
                round_reward = round_reward + self.reward[self.state][act]

                # Update Q_Value_current_state_and_action_taken = Q_Value_current_state_and_action_taken + Discounted Reward [reward_of_action_taken + (discount_factor x max_q_value - Q_value_of_action_taken_in_current_position)]
                self.Q_values[self.state][act] = self.Q_values[self.state][act] + self.lr * (self.reward[self.state][act] + self.gamma * max_Q_value - self.Q_values[self.state][act])
                #print("Taking action ", act, " to state ", next_state.state)
                # Save current position in Path list and update current_state

                self.state = next_state

            self.allrewards.append(round_reward)

    def play_sarsa_learning(self, rounds=25):
        for _ in range(rounds):
            round_reward = 0
            self.state = self.start
            random_value = random.random()
            if random_value > self.epsilon:                        
                Max_reward = [-999, -999, -999, -999]
                for M in self.actions[self.state]:        
                    Max_reward[M] = (self.Q_values[self.state][M])
                act = np.argmax(Max_reward)       
            else:
                act = random.choice(self.actions[self.state])
                    
            while self.state != self.end:
                next_state = self.n_s[self.state][act]
                random_value = random.random()
                if random_value > self.epsilon:                        
                    Max_reward = [-999, -999, -999, -999]
                    for M in self.actions[self.state]:        
                        Max_reward[M] = (self.Q_values[self.state][M])
                    next_act = np.argmax(Max_reward)       
                else:
                    next_act = random.choice(self.actions[self.state])
     

                round_reward = round_reward + self.reward[self.state][act]

                # Update Q_Value_current_state_and_action_taken = Q_Value_current_state_and_action_taken + Discounted Reward [reward_of_action_taken + (discount_factor x max_q_value - Q_value_of_action_taken_in_current_position)]
                self.Q_values[self.state][act] = self.Q_values[self.state][act] + self.lr * (self.reward[self.state][act] + self.gamma * self.Q_values[next_state][next_act] - self.Q_values[self.state][act])
                #print("Taking action ", act, " to state ", next_state.state)
                # Save current position in Path list and update current_state

                self.state = next_state
                act = next_act
                print("Next episode...")

            self.allrewards.append(round_reward)
            if self.epsilon > 0: self.epsilon -= self.e_degradation


if __name__ == "__main__":
    print("QLearning Training...")
    ag = Agent()
    ag.play_q_learning(NUM_EPISODES)
    rewards1 = ag.allrewards
    
    print("Sarsa Training...")
    ag = Agent(gamma=0.7, lr=0.7)
    ag.play_sarsa_learning(NUM_EPISODES)
    rewards2 = ag.allrewards

    plt.figure()
    plt.plot(range(NUM_EPISODES), rewards1, label="QLearning")
    plt.plot(range(NUM_EPISODES), rewards2, label="Sarsa")
    plt.title("Reward values per episode")
    plt.xlabel("Episode")
    plt.ylabel("Reward")
    plt.legend()
    plt.show()


    print("latest Q-values ... \n")
    print(ag.Q_values)