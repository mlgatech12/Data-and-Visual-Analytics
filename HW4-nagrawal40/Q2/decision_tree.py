from util import entropy, information_gain, partition_classes
from scipy import stats
import numpy as np 
import ast

class DecisionTree(object):
    def __init__(self):
        # Initializing the tree as an empty dictionary or list, as preferred
        self.tree = {
                'left': None,
                'right': None,
                'split_attr' : -1,
                'split_val' : 0,
                'info_gain' : 0,
                'label' : 0
                }
        
    def learn(self, X, y):
        # TODO: Train the decision tree (self.tree) using the the sample X and labels y
        # You will have to make use of the functions in utils.py to train the tree
        
        # One possible way of implementing the tree:
        #    Each node in self.tree could be in the form of a dictionary:
        #       https://docs.python.org/2/library/stdtypes.html#mapping-types-dict
        #    For example, a non-leaf node with two children can have a 'left' key and  a 
        #    'right' key. You can add more keys which might help in classification
        #    (eg. split attribute and split value)
        self.build_tree(X,y)
        pass
    
    def build_tree(self, X, y):

        #numerical_cols = set([0,1,2,3,4,5,6,7])
        numerical_cols = set([1, 2, 7, 10, 13, 14, 15]) # indices of numeric attributes (columns)
        if y.count(0) == len(y):
            self.tree["label"] = 0
            return 

        if y.count(1) == len(y):
            self.tree["label"] = 1
            return
        
        if len(X) == 0:
            if (y.count(0) > y.count(1)):
                self.tree["label"] = 0
                return
            else :
                self.tree["label"] = 1
                return
        
        #find best attribute to split on
        X_arr = np.array(X)
        for col in range(X_arr.shape[1] - 1):
            
            #calculate mean for numerical values
            if col in numerical_cols:	
                temp = X_arr[:,col]
                #print("temp = ", temp)
                split_val = np.mean([float(a) for a in temp])
                #print("split_val = " , split_val)
            
            #calculate mpde for numerical values
            else:			
                split_val = stats.mode(X_arr[:,col])[0][0]
                
            
            #calculate infogain
            X_left, X_right, y_left, y_right = partition_classes(X, y, col, split_val)
            current_y = []
            current_y.append(y_left)
            current_y.append(y_right)
            current_info_gain = information_gain(y, current_y)
            
            #keep max infogain arguments
            if( self.tree['info_gain'] < current_info_gain):
                self.tree['info_gain'] = current_info_gain
                self.tree['split_val'] = split_val
                self.tree['split_attr'] = col
        
        #print("current tree: " , self.tree)
        if len(X) > 0:
            self.tree["left"] = DecisionTree()
            self.tree["right"] = DecisionTree()
            
            X_left, X_right, y_left, y_right = partition_classes(X, y, self.tree['split_attr'], self.tree['split_val'])
            
            self.tree["left"].build_tree(X_left, y_left)
            self.tree["right"].build_tree(X_right, y_right)
            

    def classify(self, record):
        # TODO: classify the record using self.tree and return the predicted label
        numerical_cols = set([1, 2, 7, 10, 13, 14, 15]) # indices of numeric attributes (columns)
        
        result = 0 
        #print("tree = " , self.tree)
        col = self.tree['split_attr']
        split_val = self.tree['split_val']
        
        if self.tree["left"] == None and self.tree["right"] == None:
            result = self.tree["label"]
        
        else:
            #print("col = " , col)
            if col in numerical_cols:
                if self.tree['split_attr'] > -1 and record[self.tree['split_attr']] <= self.tree['split_val']:
                    result = self.tree["left"].classify(record)
                else:
                    result = self.tree["right"].classify(record)
            else: 
                if self.tree['split_attr'] > -1 and record[self.tree['split_attr']] == self.tree['split_val']:
                    result = self.tree["left"].classify(record)
                else:
                    result = self.tree["right"].classify(record)
        
        return str(result)
        
