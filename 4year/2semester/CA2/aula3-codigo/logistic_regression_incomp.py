# -*- coding: utf-8 -*-
"""
@author: miguelrocha
"""

import numpy as np
from dataset import Dataset

class LogisticRegression:
    
    def __init__(self, dataset, normalize = False, regularization = False, lamda = 1):
        self.X, self.y = dataset.getXy()
        self.X = np.hstack ( (np.ones([self.X.shape[0],1]), self.X ) )
        self.theta = np.zeros(self.X.shape[1])
        self.regularization = regularization
        self.lamda = lamda
        if normalize: 
            self.normalize()
        else: 
            self.normalized = False
            
    def normalize(self):
        self.mu = np.mean(self.X[:,1:], axis = 0)
        self.X[:,1:] = self.X[:,1:] - self.mu
        self.sigma = np.std(self.X[:,1:], axis = 0)
        if np.all(self.sigma!= 0): self.X[:,1:] = self.X[:,1:] / self.sigma
        self.normalized = True

    def printCoefs(self):
        print(self.theta)

    def sigmoid(self,z):
        return 1 / (1 + np.exp(-z))

    def predict(self, instance):
        prob = self.probability(instance)
        if prob > 0.5:
            return 1
        else:
            return 0
    
    def probability(self, instance):
        x = np.empty([self.X.shape[1]])
        x[0] = 1
        x[1:] = np.array(instance[:self.X.shape[1]-1])
        
        if self.normalized:
            x[1:] = (x[1:] - self.mu) / self.sigma
        z = np.dot(self.theta,x)
        return sigmoid(z)

    def costFunction(self, theta = None):
        if theta is None: theta= self.theta
        m = self.X.shape[0]
        predictions = self.sigmoid(np.dot(m,self.theta))
        acc = 0
        for i in range(0,len(m)):
            y = self.y[i]
            if y == 1:
                acc =acc - np.log(predictions[0])
            else:
                acc = acc - np.log(1 - predictions[0])
        return acc
        
    def costFunctionReg(self, theta = None, lamda = 1):
        if theta is None: theta= self.theta
        m = self.X.shape[0]
        # ...


    def gradientDescent(self, dataset, alpha = 0.01, iters = 10000):
        m = self.X.shape[0]
        n = self.X.shape[1]
        self.theta = np.zeros(n)  
        for its in range(iters):
            J = self.costFunction()
            if its%1000 == 0: print(J)
            # ...
            
            
    def buildModel(self, dataset):
        if self.regularization:
            self.optim_model()
        else:
            self.optim_model_reg(self.lamda)

    def optim_model(self):
        from scipy import optimize

        n = self.X.shape[1]
        options = {'full_output': True, 'maxiter': 500}
        initial_theta = np.zeros(n)
        self.theta, _, _, _, _ = optimize.fmin(lambda theta: self.costFunction(theta), initial_theta, **options)
    
    def optim_model_reg(self, lamda):
        from scipy import optimize

        n = self.X.shape[1]
        initial_theta = np.ones(n)        
        result = optimize.minimize(lambda theta: self.costFunctionReg(theta, lamda), initial_theta, method='BFGS', 
                                    options={"maxiter":500, "disp":False} )
        self.theta = result.x    
  

    def mapX(self):
        self.origX = self.X.copy()
        mapX = mapFeature(self.X[:,1], self.X[:,2], 6)
        self.X = np.hstack((np.ones([self.X.shape[0],1]), mapX) )
        self.theta = np.zeros(self.X.shape[1])

    def plotData(self):
        import matplotlib.pyplot as plt
        negatives = self.X[self.y == 0]
        positives = self.X[self.y == 1]
        plt.xlabel("x1")
        plt.ylabel("x2")
        plt.xlim([self.X[:,1].min(), self.X[:,1].max()])
        plt.ylim([self.X[:,1].min(), self.X[:,1].max()])
        plt.scatter( negatives[:,1], negatives[:,2], c='r', marker='o', linewidths=1, s=40, label='y=0' )
        plt.scatter( positives[:,1], positives[:,2], c='k', marker='+', linewidths=2, s=40, label='y=1' )
        plt.legend()
        plt.show()


    def plotModel(self):
        import matplotlib.pyplot as plt
        from numpy import r_
        pos = (self.y == 1).nonzero()[:1]
        neg = (self.y == 0).nonzero()[:1]
        plt.plot(self.X[pos, 1].T, self.X[pos, 2].T, 'k+', markeredgewidth=2, markersize=7)
        plt.plot(self.X[neg, 1].T, self.X[neg, 2].T, 'ko', markerfacecolor='r', markersize=7)
        if self.X.shape[1] <= 3:
            plot_x = r_[self.X[:,2].min(),  self.X[:,2].max()]
            plot_y = (-1./self.theta[2]) * (self.theta[1]*plot_x + self.theta[0])
            plt.plot(plot_x, plot_y)
            plt.legend(['class 1', 'class 0', 'Decision Boundary'])
        plt.show()

    def plotModel2(self):
        import matplotlib.pyplot as plt

        negatives = self.origX[self.y == 0]
        positives = self.origX[self.y == 1]
        plt.xlabel("x1"); plt.ylabel("x2")
        plt.xlim([self.origX[:,1].min(), self.origX[:,1].max()])
        plt.ylim([self.origX[:,1].min(), self.origX[:,1].max()])
        plt.scatter( negatives[:,1], negatives[:,2], c='r', marker='o', linewidths=1, s=40, label='y=0' )
        plt.scatter( positives[:,1], positives[:,2], c='k', marker='+', linewidths=2, s=40, label='y=1' )
        plt.legend()

        u = np.linspace( -1, 1.5, 50 )
        v = np.linspace( -1, 1.5, 50 )
        z = np.zeros( (len(u), len(v)) )

        for i in range(0, len(u)): 
            for j in range(0, len(v)):
                x = np.empty([self.X.shape[1]])  
                x[0] = 1
                mapped = mapFeature( np.array([u[i]]), np.array([v[j]]) )
                x[1:] = mapped
                z[i,j] = x.dot( self.theta )
        z = z.transpose()
        u, v = np.meshgrid( u, v )	
        plt.contour( u, v, z, [0.0, 0.001])
        plt.show()


def sigmoid(x):
  return 1 / (1 + np.exp(-x))
  
def mapFeature(X1, X2, degrees = 6):
	out = np.ones( (np.shape(X1)[0], 1) )
	
	for i in range(1, degrees+1):
		for j in range(0, i+1):
			term1 = X1 ** (i-j)
			term2 = X2 ** (j)
			term  = (term1 * term2).reshape( np.shape(term1)[0], 1 ) 
			out   = np.hstack(( out, term ))
	return out  
  
  

# main - tests
def test():
    ds= Dataset("log-ex1.data")   
    logmodel = LogisticRegression(ds)
    logmodel.plotData()    
    print ("Initial cost: ", logmodel.costFunction())
    # result: 0.693

    logmodel.gradientDescent(ds, 0.001, 200000)
    
    #logmodel.optim_model()
    
    logmodel.plotModel()
    print ("Final cost:", logmodel.costFunction())
    
    ex = np.array([45,65])
    print ("Prob. example:", logmodel.probability(ex))
    print ("Pred. example:", logmodel.predict(ex))
    

def testreg():
    ds= Dataset("log-ex2.data")   
       
    logmodel = LogisticRegression(ds)
    logmodel.plotData()
    logmodel.mapX()
    logmodel.printCoefs()

    print (logmodel.costFunction())
    logmodel.optim_model_reg(0.1)
    logmodel.printCoefs()
    print (logmodel.costFunction())
    logmodel.plotModel2()    
    

if __name__ == '__main__':
    #test()
    testreg()

