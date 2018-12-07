-- (1) Write MySQL query to find IPs that mode more than a certain number of requests for a given time period.
-- Ex: Write SQL to find IPs that made more than 100 requests starting from 2017-01-01.13:00:00 to 2017-01-01.14:00:00.

select ip_address
from log_entry
where '2017-01-01 13:00:00' <= date_time
  and date_time < '2017-01-01 14:00:00'
group by ip_address
having count(ip_address) > 100;

-- (2) Write MySQL query to find requests made by a given IP.
select *
from log_entry
where ip_address = '192.168.228.188';