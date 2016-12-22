import pyrebase
import os
config = {
  "apiKey": "AIzaSyABwNgjE8n4dZ7ts6dsCWe7o3QyCLz7-1w",
  "authDomain": "connect-e3b5b.firebaseapp.com",
  "databaseURL": "https://connect-e3b5b.firebaseio.com",
  "storageBucket": "connect-e3b5b.appspot.com"
}

firebase = pyrebase.initialize_app(config)


files = [f for f in os.listdir('.') if os.path.isfile(f)]
sendFiles = []
for f in files:
	if str(f) != "con.py" and str(f) != ".DS_Store":
		sendFiles.append(str(f)) #does not get un-needed files
db = firebase.database()

already = str(db.child("RetrievedFiles").get().val())
#print already+"\n\n"
arr = []
try:
	begin = already.find(', u"')+4 #gets retrieved files
	end = -4
	arr = eval(already[begin:end])
except:
	pass
storage = firebase.storage()
hist = []
already = str(db.child("History").get().val())


begin = already.find(", u'")+4 #gets retrieved files
end = -4
an = already[begin:end].replace("\\'",'"')

hist = an.split(",")
# as admin
for s in sendFiles:
	if s not in arr:
		print str(s)+" Downloaded"
		storage.child(s).put(s)

for fileD in hist:
	try:
		actualFile = fileD[fileD.find("'")+1:fileD.rindex("'")]
		if actualFile not in sendFiles and not fileD.startswith("eredDict([(u'"):
			print str(actualFile) +" Deleted"
			storage.child(str(fileD)).delete()
	except:
		pass
db.child("GivenFiles").remove()
db.child("GivenFiles").push(str(sendFiles))

db.child("History").remove()
db.child("History").push(str(sendFiles))




