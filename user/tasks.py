from celery.decorators import task
from user.models import Payment, Subscription
sms = SMS(client_id="", client_secret="")
from sentry_sdk import capture_exception, capture_message


from datetime import timedelta
from utils.asoriba import Asoriba
cbs = Asoriba("pubkey", "webhook url", "AgedCare", "Service Payment")


@task(name="monthly_subscription")
def monthly_subscription():
    customers = Subscription.objects.filter(subscription_type='monthly').values("package_type", "phonenumber", "firstname", "lastname")
    for user in customers:
        if user["package_type"] == "single":
            cbs.initialize("15", user["firstname"], user["lastname"], user["phonenumber"])
            
        if user["package_type"] == "double":
            cbs.initialize("27", user["firstname"], user["lastname"], user["phonenumber"])

        if user["package_type"] == "full":
            cbs.initialize("41", user["firstname"], user["lastname"], user["phonenumber"])
 
        

@task(name="three_months_subscription")
def three_months_subscription():
    customers = Subscription.objects.filter(subscription_type='three_months').values("package_type", "phonenumber", "firstname", "lastname")
    
    for user in customers:
        if user["package_type"] == "single":
            cbs.initialize("45", user["firstname"], user["lastname"], user["phonenumber"])
            
        if user["package_type"] == "double":
            cbs.initialize("81", user["firstname"], user["lastname"], user["phonenumber"])

        if user["package_type"] == "full":
            cbs.initialize("123", user["firstname"], user["lastname"], user["phonenumber"])


@task(name="six_months_subscription")
def six_months_subscription():
    customers = Subscription.objects.filter(subscription_type='six_months').values("package_type", "phonenumber", "firstname", "lastname")
    
    for user in customers:
        if user["package_type"] == "single":
            cbs.initialize("90", user["firstname"], user["lastname"], user["phonenumber"])
            
        if user["package_type"] == "double":
            cbs.initialize("162", user["firstname"], user["lastname"], user["phonenumber"])

        if user["package_type"] == "full":
            cbs.initialize("246", user["firstname"], user["lastname"], user["phonenumber"])
            

@task(name="annual_subscription")
def annual_subscription():
    customers = Subscription.objects.filter(subscription_type='annual').values("package_type", "phonenumber", "firstname", "lastname")
    for user in customers:
        if user["package_type"] == "single":
            cbs.initialize("180", user["firstname"], user["lastname"], user["phonenumber"])
            
        if user["package_type"] == "double":
            cbs.initialize("324", user["firstname"], user["lastname"], user["phonenumber"])

        if user["package_type"] == "full":
            cbs.initialize("492", user["firstname"], user["lastname"], user["phonenumber"])