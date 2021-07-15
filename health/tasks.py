from celery.decorators import task
from user.models import CustomUser
from health.models import Pressure, PressureDataResult
from pyhubtel_sms import SMS, Message
from django.db.models import Sum, Count, FloatField
from django.db.models.functions import Cast

sms = SMS(client_id="", client_secret="")
from sentry_sdk import capture_exception, capture_message


import datetime
from datetime import timedelta
date = datetime.date.today()

today = datetime.date.today()
start_week = date - datetime.timedelta(date.weekday())
end_week = start_week + datetime.timedelta(7)

@task(name="weekly_pressure_report")
def weekly_pressure_report():
    customer = CustomUser.objects.values('phonenumber')
    for phonenumber in customer:
        # aggreate for the weekly and do the calculation
        diastolic = Pressure.objects.filter(created_at__range=[start_week, end_week]).filter(username=phonenumber['phonenumber']).annotate(as_float=Cast('diastolic', FloatField()).aggregate(Sum('diastolic'))
        systolic = Pressure.objects.filter(created_at__range=[start_week, end_week]).filter(username=phonenumber['phonenumber']).annotate(as_float=Cast('systolic', FloatField()).aggregate(Sum('systolic'))
        
        if systolic:
            systolic_count =  Pressure.objects.filter(created_at__range=[start_week, end_week]).filter(username=phonenumber['phonenumber']).values_list('systolic').count()
            sysotlic_total = systolic['systolic__sum'] / systolic_count
        else:
            capture_message("error")
            
            
        if diastolic:
            diastolic_count =  Pressure.objects.filter(created_at__range=[start_week, end_week]).filter(username=phonenumber['phonenumber']).values_list('diastolic').count()
            diastolic_total = diastolic['diastolic__sum'] / diastolic_count
            try:
                total = sysotlic_total + "/" + diastolic_total
                PressureDataResult.objects.create(
                    phonenumber=phonenumber['phonenumber'],
                    result=total,
                    task_category='weekly',
                    sms_category='processing',
                )
                # send message
                
            except Exception as e:
                capture_exception(e)
        else:
            capture_message("error")
    

    

@task(name="monthly_pressure_report")
def monthly_pressure_report():
    customer = CustomUser.objects.values('phonenumber')
    for phonenumber in customer:
        diastolic = Pressure.objects.filter(created_at__year=today.year, created_at__month=today.month).filter(username=phonenumber['phonenumber']).annotate(as_float=Cast('diastolic', FloatField()).aggregate(Sum('diastolic'))
        systolic = Pressure.objects.filter(created_at__year=today.year, created_at__month=today.month).filter(username=phonenumber['phonenumber']).annotate(as_float=Cast('systolic', FloatField()).aggregate(Sum('systolic'))
        
        if systolic:
            systolic_count =  Pressure.objects.filter(created_at__year=today.year, created_at__month=today.month).filter(username=phonenumber['phonenumber']).values_list('systolic').count()
            sysotlic_total = systolic['systolic__sum'] / systolic_count
        else:
            capture_message("error")
            
        
        if diastolic:
            diastolic_count =  Pressure.objects.filter(created_at__year=today.year, created_at__month=today.month).filter(username=phonenumber['phonenumber']).values_list('diastolic').count()
            diastolic_total = diastolic['diastolic__sum'] / diastolic_count
            try:
                total = sysotlic_total + "/" + diastolic_total
                PressureDataResult.objects.create(
                    phonenumber=phonenumber['phonenumber'],
                    result=total,
                    task_category='monthly',
                    sms_category='processing',
                )
                # send message
            except Exception as e:
                capture_exception(e)
        else:
            capture_message("error")

def send_result(phonenumber=None, task_category=None):
    phonenumber = CustomUser.objects.get(username=phonenumber)
    task_cateogry = PressureDataResult.objects.get(phonenumber=phonenumber).filter(task_category=task_category).values('weekly')
    result = PressureDataResult.objects.get(phonenumber=phonenumber).filter(sms_category='processing').values('result')
    
    for phonenumber in phonenumber:
        for result in result:
            for task in task_cateogry:
                try:
                    sms.send_message(sender="", recipient=phonenumber, content="Your {} bmi result {}".format(task, result), registered_delivery=True)
                    result.sms_category = 'sent'
                    result.save()
                except Exception as e:
                    capture_exception(e)