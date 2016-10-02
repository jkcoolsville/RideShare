#!/usr/bin/python
import cgi
import cgitb; cgitb.enable()
import os
import sys
from math import radians, cos, sin, asin, sqrt
from socket import gethostname, gethostbyname
print("Content-Type: text/html;charset=utf-8\r\n")
print("Bam!<br>\r\n")
form = cgi.FieldStorage()
print("\r\n")

def haversine(lon1, lat1, lon2, lat2):
    """
    Calculate the great circle distance between two points 
    on the earth (specified in decimal degrees)
    """
    # convert decimal degrees to radians 
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])

    # haversine formula 
    dlon = lon2 - lon1 
    dlat = lat2 - lat1 
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a)) 
    r = 3956  # 6371 for km # Radius of earth in kilometers. Use 3956 for miles
    return c * r


#register:
#	name (string)
#	car (1/0)
#	seats available (int)
#	radius (int)
#	timegoing (4 digits, 24 hour format time)
#	timeleaving (4 digits, 24 hour format time)
#	email (string)
#	password (string)
#	facebook (string)
#
# name:bob.smith((car:1((seats:3((radius:5((timegoing:0930((timeleaving:1300((email:name@example.com((password:supersecret
#TODO refuse registering if email address already exists in db
if "register" in form:
	print("Got stuff in register:<br>\r\n")
	payload = form["register"].value
	print(payload)
	print("<br>\r\n")
	
	myfile = open("log.txt", "a")
	myfile.write(payload)
	myfile.write("\r\n")
	myfile.close()

	parsed = payload.split("((")
	for x in parsed:
		print(x)
		print("<br>")
	
	dbfile = open("sdb.txt", "a")
	dbfile.write(payload)
	dbfile.write("\r\n")
	dbfile.close()

#edit:
#	car (string)
#	email (string)
#	password (string)
#
#TODO: everything
elif "edit" in form:
	print("<br>Got stuff in edit:<br>\r\n")
	payload = form["edit"].value
	print("<br>")
	print(payload)
	print("<br>\r\n")

	myfile = open("log.txt", "a")
	myfile.write("edit="+payload)
	myfile.write("\r\n")
	myfile.close()

#requestride:
#	startpoint (string (address or lat/long))
#	endpoint (string (address or lat/long))
#
#TODO: everything
elif "requestride" in form:
	print("<br>Got stuff in requestride:<br>\r\n")
	payload = form["requestride"].value
	print("<br>")
	print(payload)
	print("<br>\r\n")

	myfile = open("log.txt", "a")
	myfile.write("requestride="+payload)
	myfile.write("\r\n")
	myfile.close()



#feedback:
#	enjoyment (int -3 to 3
#	condition (int 1 to 4)
#	ability (int 1 to 4)
#
#TODO: everything
elif "feedback" in form:
	print("<br>Got stuff in feedback:<br>\r\n")
	payload = form["feedback"].value
	print("<br>")
	print(payload)
	print("<br>\r\n")

	myfile = open("log.txt", "a")
	myfile.write("feedback="+payload)
	myfile.write("\r\n")
	myfile.close()

#login:
#	email (string)
#	password (string)
#	facebook (string)
#
#TODO: use cookies and session handling instead of raw cgi
elif "login" in form:
	#print("<br>Got stuff in login:<br>\r\n")
	payload = form["login"].value
	attempt = payload.split("((")
	aemail = attempt[0].split(":")[1]
	apass = attempt[1].split(":")[1]

	myfile = open("log.txt", "a")
	myfile.write("login="+payload)
	myfile.write("\r\n")
	myfile.close()
	
	mustupdatelogin = 0
	myfile = open("ipemail.txt", "r")
	log = myfile.read().split("\r\n")
	for line in log:
		if os.environ["REMOTE_ADDR"] in line:
			mustupdatelogin = 1
	myfile.close()

	if mustupdatelogin:
		myfile = open("ipemail.txt", "w")
		for line in log:
			if os.environ["REMOTE_ADDR"] not in line:
				if len(line) > 2:
					myfile.write(line+"\r\n")
			else:
				dbfile = open("sdb.txt", "r")
		                entries = dbfile.read().split("\r\n")
        		        emailmatched = 0
                		for entry in entries:
                        		elements = entry.split("((")
                        		if len(elements) > 0:
                                		if "email:"+aemail in elements:
                                        		emailmatched = 1
                                        		if "password:"+apass in elements:
                                                		print "Login Successful.<br>"
                                                		ip = os.environ["REMOTE_ADDR"]
                                                		myfile.write(aemail+"(("+ip+"\r\n")
                                        		else:
                                                		print "Invalid Password.<br>"
                                        		break
                		dbfile.close()
                		if emailmatched == 0:
                        		print "Invalid Login.<br>\r\n"

                myfile.close()


	else:
		dbfile = open("sdb.txt", "r")
		entries = dbfile.read().split("\r\n")
		emailmatched = 0
		for entry in entries:
			elements = entry.split("((")
			if len(elements) > 0:
				if "email:"+aemail in elements:
					emailmatched = 1
					if "password:"+apass in elements:
						print "Login Successful.<br>"
						#ip = gethostbyname(gethostname())
						ip = os.environ["REMOTE_ADDR"]
						myfile = open("ipemail.txt", "a")
						myfile.write(aemail+"(("+ip+"\r\n")
						myfile.close()
						#print ip
					else:
						print "Invalid Password.<br>"
					break
		dbfile.close()
		if emailmatched == 0:
			print "Invalid Login.<br>\r\n"
				
	

#getmatches:
#	directmessage (string id/email)
# a rider hopeful will hit this, their location is forwarded to the database
#should return all drivers with radii within range
elif "getmatches" in form:
	#print("<br>Got stuff in getmatches:<br>\r\n")
	payload = form["getmatches"].value
	#print("<br>")
	#print(payload)
	#print("<br>\r\n")

	myfile = open("log.txt", "a")
	myfile.write("getmatches="+payload)
	myfile.write("\r\n")
	myfile.close()

	from math import *

	Aaltitude = 2000
	Oppsite  = 20000

	
	ip = os.environ["REMOTE_ADDR"]
	myfile = open("ipemail.txt", "r")
	#myfile.write(aemail+" "+ip+"\r\n")
	entries = myfile.read().split("\r\n")
	#print entries
	#print "<br>"
	matched = 0
	for entry in entries:
		if len(entry) > 2:
			#print "entry is "
			#print entry
			#print "<br>"
			for i in entry.split("(("):
				if entry.split("((")[1] == ip:
					print "Listing matches for "
					print i
					print "<br>"
					matched = i
					break
		if matched:
			break
	#for entry in entries:
	myfile.close()
	
	if not matched:
		print "Not logged in.<br>\r\n"
		sys.exit()

	dbfile = open("sdb.txt", "r")
        entries = dbfile.read().split("\r\n")
        emailmatched = 0
	wantedentry = 0
        for entry in entries:
                elements = entry.split("((")
                if len(elements) > 0:
                        if "email:"+str(matched) in elements:
                                emailmatched = 1
				wantedentry = elements
                                break
	for thing in wantedentry:
		if thing.split(":")[0] == "lat":
			lat1 = float(thing.split(":")[1])
		elif thing.split(":")[0] == "lon":
			lon1 = float(thing.split(":")[1])
        
        for entry in entries:
                elements = entry.split("((")
		outstring = ""
		car = 0
		for thing in elements:
			if thing.split(":")[0] == "email":
				outstring+=" email:"
				outstring+=thing.split(":")[1]
			elif thing.split(":")[0] == "lat":
				lat2 = float(thing.split(":")[1])
			elif thing.split(":")[0] == "lon":
				lon2 = float(thing.split(":")[1])
			elif thing.split(":")[0] == "timegoing":
				outstring+=" timegoing:"
				outstring+=thing.split(":")[1]
			elif thing.split(":")[0] == "timeleaving":
				outstring+=" timeleaving:"
				outstring+=thing.split(":")[1]
			elif thing.split(":")[0] == "seats":
				outstring+=" seats:"
				outstring+=thing.split(":")[1]
			elif thing.split(":")[0] == "radius":
				radius = float(thing.split(":")[1])
			elif thing.split(":")[0] == "car":
				car = thing.split(":")[1]
			elif thing.split(":")[0] == "name":
				outstring+="name:"
				outstring+=thing.split(":")[1]

		distance = haversine(lon1, lat1, lon2, lat2)
		if distance <= radius and car == "1":
			outstring+=" distance:"
			outstring+=str(distance)
			outstring+="<br>\r\n"
			print outstring

	dbfile.close()
