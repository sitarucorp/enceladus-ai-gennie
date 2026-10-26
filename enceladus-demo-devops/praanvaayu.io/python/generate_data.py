from datetime import datetime, timedelta
import random

# This script generates random sql data.sql for 1000 fake ips
# to be use to demonstrate UI for IP address & other details associated from ipinfo.io
tenants = ['tenant1', 'tenant2', 'tenant3', 'tenant4', 'tenant5']
cities = [
    ('Mumbai', 'MH', '400001'), ('Delhi', 'DL', '110001'), ('Chennai', 'TN', '600001'),
    ('Bangalore', 'KA', '560001'), ('Pune', 'MH', '411001'), ('Kolkata', 'WB', '700001'),
    ('Hyderabad', 'TG', '500001'), ('Ahmedabad', 'GJ', '380001'), ('Patna', 'BR', '800001'),
    ('Jaipur', 'RJ', '302001')
]

start_time = datetime(2025, 6, 1, 0, 0, 0)

with open('data.sql', 'w') as f:
    f.write("-- Auto-generated 1000 rows\n")
    for i in range(1, 1001):
        ip = f"'192.168.{i // 256}.{i % 256}'"
        report_time = start_time + timedelta(minutes=i)
        report_date = f"'{report_time.isoformat()}'"
        source = random.choice(['internal', 'external'])
        tenant = random.choice(tenants)
        country = 'India'
        region = random.choice(['North', 'South', 'East', 'West'])
        city, state, zip_code = random.choice(cities)
        blacklisted = random.choice(['true', 'false'])
        black_list_date = f"'{(report_time + timedelta(days=1)).isoformat()}'" if blacklisted == 'true' else 'null'

        sql = f"INSERT INTO ip_address_report (ip_address, report_date, source, tenant, country, region, city, state, zip, blacklisted, black_list_date) VALUES ({ip}, {report_date}, '{source}', '{tenant}', '{country}', '{region}', '{city}', '{state}', '{zip_code}', {blacklisted}, {black_list_date});\n"
        f.write(sql)
