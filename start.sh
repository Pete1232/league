#!/bin/bash
echo "Changing to /tmp directory"
cd /tmp
echo "Starting application server"
sudo java -jar -Drun.mode=Prod -Dhttp.port=80 league-assembly-0.1-SNAPSHOT.jar &
echo "App server should be running soon"
