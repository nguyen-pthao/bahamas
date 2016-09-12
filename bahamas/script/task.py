import smtplib
import ConfigParser
import datetime
import sys
import mysql.connector
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

def send_email( from_address,to_address ):
	try:
		fromaddr = from_address
		toaddr = to_address
		msg = MIMEMultipart()
		msg['From'] = fromaddr
		msg['To'] = toaddr
		msg['Subject'] = "Python subject email"
		
		body = "Python test body mail"
		msg.attach(MIMEText(body, 'plain'))
		
		server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
		server.ehlo()
		server.login(email_username,email_password) 
		server.sendmail(fromaddr, toaddr, msg.as_string())
		
		print str(now) + ' Email sent'
	except:
		print str(now) + ' Send email failed'
	finally:
		server.quit()
		return

now = datetime.datetime.now()

email_props_file = 'C:/Java/twc/bahamas/build/web/WEB-INF/classes/email.properties'
db_props_file = 'C:/Java/twc/bahamas/build/web/WEB-INF/classes/connection.properties'

config = ConfigParser.RawConfigParser()

try:
	config.read(email_props_file)
	email_username = config.get('Email','email.username')
	email_password = config.get('Email','email.password')

	config.read(db_props_file)
	db_host = config.get('Connection','db.host')
	db_port = config.get('Connection','db.port')
	db_name = config.get('Connection','db.name')
	db_user = config.get('Connection','db.user')
	db_password = config.get('Connection','db.password')

except:
	print str(now) + ' Error reading properties files'
	sys.exit(0)
	

try:
	#retrieve db data
	# Open database connection
	conn = mysql.connector.connect(user=db_user,password=db_password,database=db_name,host=db_host,port=db_port)
	
	# prepare a cursor object using cursor() method
	cursor = conn.cursor()

	#Prepare query
	query = """SELECT EMAIL,MINIMUM_PARTICIPATIONS,PARTICIPANT_NUMBER,EVENT_TITLE,CREATED_BY FROM EVENT WHERE CURDATE() < EVENT_START_DATE AND
	DATE_ADD(CURDATE(), INTERVAL 3 DAY) <= DATE(EVENT_START_DATE)
	AND PARTICIPANT_NUMBER < MINIMUM_PARTICIPATIONS AND SEND_REMINDER=TRUE AND EVENT_STATUS='Open'"""

	# execute SQL query using execute() method.
	cursor.execute(query)

	# Fetch a single row using fetchone() method.
	for email, min_pax,current_pax,title,creator in cursor.fetchall():
		#do something here
	
	#send_email(email_username,"huxley.goh.2014@smu.edu.sg")

except:
	print str(now) +  " Error accessing database"
	sys.exit(0)
	
finally:
	cursor.close()
	conn.close()
	

