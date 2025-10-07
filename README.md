[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/wc252Yhc)
# DMIT2015 Assignment Discussion

## Student Name: Youfang Yao, Github UserName: Hansenyao

Write your assignment discussion here with the status of your assignment, time to complete, any issues encountered, any key learning points, and any feedback on improving the assignment.

## Overview

### Assignment02 includes below features

- User sign up
- User sign in and sign out
- Bike CRUD operations on Firebase
- Data isolated on Firebase for each user

### There are 3 values in file microprofile-config.properties 

- The base url for Bike data on Firebase

```
firebase.rtdb.bike.base.url=https://dmit2015-test-default-rtdb.firebaseio.com
```
- The base url for authentication

```
firebase.auth.baseUrl=https://identitytoolkit.googleapis.com/v1
```

- The API Key

```
firebase.web.api.key=AIzaSyAAsaVCqyLsChpgHMWCLSDw8Rsd0AjKkRs
```

### Data Path Explanation

- In multi-tenant model, each user's data is saved in isolated path on Firebase.

- Data path like: /multi_tenant_data/<Entity>/{uid}/

  - multi_tenant_data: The root folder for multi-tenant application

  - <Entity>: Data model name, for example "Bike"

  - {uid}: User ID string, for example "8cUswbq74hhMD6bZ7AyvTv3zxK02"
