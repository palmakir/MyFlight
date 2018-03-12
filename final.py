# -*- coding: utf-8 -*-
"""
Created on Mon Mar 12 04:33:28 2018

@author: admin
"""

import numpy as np
import matplotlib.pyplot as plt
import pandas as pd
import math
import requests
 
# api-endpoint
URL = 'http://pruthveshdesai.000webhostapp.com/receiveCIN.php'


 
# defining a params dict for the parameters to be sent to the API
PARAMS = {}
r = requests.get(url = URL, params = PARAMS)
data_json = r.json()
print(data_json)


pnr=[]
age=[]
seat=[]

for i in range(0,len(data_json)):
   dict=data_json[i]
   pnr.append(dict['pnr'])
   age.append(dict['data2'])
   seat.append(dict['seat'])

un_alloted=[]
age_parser=[]
booked=[]
booked_t_age=[]
#Cleaning the age data
for j in range(0,len(age)):
    x=age[j].split(' ')
    age[j]=int(x[0])
    
for i in range(0,len(seat)):
    if(seat[i]==0):
        un_alloted.append(pnr[i])
        age_parser.append(age[i])
    else:
        booked.append(seat[i])
        
        
#Coverting the age_parser into numpy array
#b=np.asarray(age_parser)
numpy_ages=np.reshape(age_parser, (len(age_parser), 1))


#Now traing the model

# Importing the dataset
dataset = pd.read_csv('airplane.csv')
X = dataset.iloc[:, 2:3].values
pnr=dataset.iloc[:,0]
y = dataset.iloc[:, 3].values

# Splitting the dataset into the Training set and Test set
'''from sklearn.cross_validation import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.2, random_state = 0)'''

# Feature Scaling
"""from sklearn.preprocessing import StandardScaler
sc_X = StandardScaler()
X_train = sc_X.fit_transform(X_train)
X_test = sc_X.transform(X_test)"""

# Fitting Linear Regression to the dataset
from sklearn.linear_model import LinearRegression
lin_reg = LinearRegression()
lin_reg.fit(X, y)

# Fitting Polynomial Regression to the dataset
from sklearn.preprocessing import PolynomialFeatures
poly_reg = PolynomialFeatures(degree = 5)
X_poly = poly_reg.fit_transform(X)
poly_reg.fit(X_poly, y)
lin_reg_2 = LinearRegression()
lin_reg_2.fit(X_poly, y)

# Visualising the Linear Regression results
plt.scatter(X, y, color = 'red')
plt.plot(X, lin_reg.predict(X), color = 'blue')
plt.title('Seat predictor (Linear Regression)')
plt.xlabel('Age')
plt.ylabel('Seat Alloted')
plt.show()

# Visualising the Polynomial Regression results
plt.scatter(X, y, color = 'red')
plt.plot(X, lin_reg_2.predict(poly_reg.fit_transform(X)), color = 'blue')
plt.title('Seat predictor (Polynomial Regression)')
plt.xlabel('Age')
plt.ylabel('Seat Alloted')
plt.show()

# Visualising the Polynomial Regression results (for higher resolution and smoother curve)
X_grid = np.arange(min(X), max(X), 0.1)
X_grid = X_grid.reshape((len(X_grid), 1))
plt.scatter(X, y, color = 'red')
plt.plot(X_grid, lin_reg_2.predict(poly_reg.fit_transform(X_grid)), color = 'blue')
plt.title('Seat predictor (polynomial Regression)')
plt.xlabel('Age')
plt.ylabel('Seat Alloted')
plt.show()

#Predicting a new result with Polynomial Regression

y_pred=lin_reg_2.predict(poly_reg.fit_transform(numpy_ages))

final_allotment=[]
for x in np.nditer(y_pred):
    final_allotment.append(math.ceil(x))

#Posting the results into the database
    
import urllib.request, urllib.parse

for i in range(0,len(final_allotment)):
    p=un_alloted[i]
  
    s=final_allotment[i]
    data = {'pnr' : str(p), 'seat' : str(s)}
    data = bytes( urllib.parse.urlencode( data ).encode() )
    handler = urllib.request.urlopen( 'https://pruthveshdesai.000webhostapp.com/add_seats.php', data );



        