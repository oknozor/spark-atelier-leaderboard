# Spark Atelier Leaderboard

## Run

#### Backend
- `git clone ...`
- `cd spark-atelier-leaderboard/back`
- `vertx run vertx run src/main/java/org/univ/sparktp/dashboard/MainVerticle.java`

#### Frontend
- `cd ../front`
- `npm install`
- `npm start`


## Note 

Each team start with a zero score. There are only two actions mutating the 
State off the app : creating a team, add completing a step. 
On step completion a team earns 50 points. Steps have no identifier, either you go forward and earn 50 points or nothing happen.

## Endpoints 

- `POST /teams`:
    - body : `{ "name": "Les Daniels" }`
- `GET /teams`: Get all teams
- `POST /teams/{id}/stepCompleted`: earn points for completing a step
- `POST /teams/{id}/stepFailed`: Shame the team for failing the current step

## Model 

#### Team

```
{
 "id": 1, 
 "name": "Les patricks",
 "score" : 0
}
```

### Todo: 

1. Add database persistence so we can restore the state of the ongoing session in case of crash. 
2. Add a shamefull message on step failure 
3. Design the cli

