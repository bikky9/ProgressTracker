# Progress Tracker

Download the Django code from [here](https://nikhilbikky@git.cse.iitb.ac.in/pavanchaitanya/ProgressTracker.git)


## Admin Guide:


Install Django, Complete Instructions can be found [here](https://www.djangoproject.com/download/)


Install Django-REST framework, Complete Instructions can be found [here](https://www.django-rest-framework.org/#installation)

Go to the Project directory and makemigrations:

```
python3 manage.py makemigrations
python3 manage.py migrate
```

Now we are ready to start local server

```
python3 manage.py runserver < ip-address >
```
Now setup your(Admin) account by creating a super user, type the following in terminal and rest is self explanatory
```
python3 manage.py createsuperuser
```
For adding pre-registered Instructors visit the django-admin site - `http://localserver/polls/admin`


## Instructor Guide:

Visit `polls/login` and login to the account created by the admin

Each instructor can create, activate, de-activate or delete Tracking sessions from the website after logging in

Students can be added through Comma-Seperated-Values(CSV) file or by adding individually

Question can be added followed by checkpoints under each question

Instructor gives certain number of questions for that particular session and the progress of added students is tracked via checkpoints under each question

## Student Guide:

Install the ProgressTracker app from this repository

Login with the credentials provided by your professor

Any Navigation after clicking the radio button will update the checkpoint

**Comment**: Its like a feedback or citicism 

**Ping**: Its a chat feature with the instructor, all the pings from every student queues up at the instructor
