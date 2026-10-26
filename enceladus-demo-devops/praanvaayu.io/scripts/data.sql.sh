#!/bin/bash
echo "-- H2-compatible auto-generated data.sql" > data.sql
for i in {1..100}; do
  echo "INSERT INTO task (description, creation_date, due_date) VALUES ('Task $i description', NOW(), DATEADD('DAY', $i, CURRENT_DATE));" >> data.sql
done
