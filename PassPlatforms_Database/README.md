# Setting up Database

## Basic Setup
* install oracle SQL (v 21) using your favrouit methods
* docker instructions: https://medium.com/geekculture/run-oracle-database-21c-in-docker-351049344d0c

## Scheme setup
* Create schema user `PP_ADMIN`, or replace scheme name in SQL script `PassPlatformsDB.sql`

```
CREATE USER pp_admin identified BY pp_admin
```
* assign the privlages (or customize them as fits)
```
grant sysdba to pp_admin
```

* run the SQL script `PassPlatformsDB.sql` to construct the DB

## System Mnager & Admin
* to add a manager to the admin, use the following command (replace `USER_ID` with actual login id used for moodle/ssb/etc), (`ROLE_ID`: 4 for manager, 5 for admin)
```
insert into user values ('USER_ID',ROLE_ID)
```

## Statistic Trigger
* the database caches in the statistics of the platform every night using a procedure, to setup the time procedure please run the following script as a sys admin 

```
BEGIN
  -- Create the scheduled job
  DBMS_SCHEDULER.CREATE_JOB(
    job_name        => 'DAILY_FETCH_STATS_JOB',
    job_type        => 'PLSQL_BLOCK',
    job_action      => 'BEGIN FETCH_STATS; END;',
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'FREQ=DAILY; BYHOUR=20; BYMINUTE=0; BYSECOND=0;',
    enabled         => TRUE
  );

  -- Commit the changes
  COMMIT;
END;
/
```
