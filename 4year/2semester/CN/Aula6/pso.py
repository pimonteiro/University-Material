import numpy as np
import matplotlib.pyplot as plt

def error(current_pos):
    x = current_pos[0]
    y = current_pos[1]

    return (x ** 2 - 10 * np.cos(2 * np.pi * x)) + \
           (y ** 2 - 10 * np.cos(2 * np.pi * y)) + 20


def grad_error(current_pos):
    x = current_pos[0]
    y = current_pos[1]

    return np.array(
        [2 * x + 10 * 2 * np.pi * x * np.sin(2 * np.pi * x),
         2 * y + 10 * 2 * np.pi * y * np.sin(2 * np.pi * y)])
    

class Particle:
    
    def __init__(self, dim, world_size):
        self.pos = np.random.uniform(size=dim, low=world_size[0], high=world_size[1])
        self.best_local_pos = self.pos.copy()
        
        self.error = error(self.pos)
        self.best_local_err = self.error.copy()
        
    def setPos(self, pos):
        self.pos = pos
        self.error = error(pos)
        if self.error < self.best_local_err:
            self.best_local_pos = pos
            self.best_local_err = self.error


class PSO:
    w  = 5
    c1 = 0.5
    c2 = 0.5
    lr = 0.2
    
    def __init__(self, dims, pop, epochs, world_size):
        self.swarm = [Particle(dims, world_size) for i in range(pop)]
        self.epochs = epochs
        
        self.best_swarm_pos = np.random.uniform(size=dims, low=world_size[0], high=world_size[1])
        self.best_swarm_err = 1e80
        
    def optimize(self):
        for i in range(self.epochs):
            for j in range(len(self.swarm)):
                particle = self.swarm[j]
                
                vel = grad_error(particle.pos)
                
                deltaV = self.w * vel \
                    + self.c1 * (particle.best_local_pos - particle.pos) \
                    + self.c2 * (self.best_swarm_pos - particle.pos)
                
                new_pos = self.swarm[j].pos - self.lr * deltaV
                
                self.swarm[j].setPos(new_pos)
                
                if error(new_pos) < self.best_swarm_err:
                    self.best_swarm_pos = new_pos
                    self.best_swarm_err = error(new_pos)
                    
            print('Epoch: {0} | Best position: [{1},{2}] | Best known error: {3}'.format(i,
                                                                                     self.best_swarm_pos[0],
                                                                                     self.best_swarm_pos[1],
                                                                                     self.best_swarm_err))
            
     
pso = PSO(dims=2, pop=30, epochs=500, world_size=[-500,500])
pso.optimize()