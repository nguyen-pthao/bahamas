import smtplib
import ConfigParser


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
	print 'Error reading properties files'
	sys.exit(0)

//retrieve db data

	
try:
	server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
	server.ehlo()
	server.login(email_username,email_password)
	 
	msg = "YOUR MESSAGE! TESTING!!!!"
	server.sendmail(email_username, "huxley.goh.2014@smu.edu.sg", msg)
	server.quit()
	
	print 'Email sent!'
	
except:
	print 'Something went wrong, send email fail'