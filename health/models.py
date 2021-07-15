from django.db import models
from django.utils.translation import gettext as _
from careapp.models import UserModel
from decimal import Decimal


# Create your models here.
class HealthData(models.Model):
    userId = models.CharField(_("User ID"), max_length=50, null=True, blank=True)
    user = models.ForeignKey(UserModel, on_delete=models.CASCADE)
    phonenumber = models.IntegerField(_("Phone Number"))
    bloodsugar = models.IntegerField(_("Blood Sugar"))
    bloodpressure = models.IntegerField(_("Blood Pressure"))
    bloodcholesterol = models.IntegerField(_("Blood Cholesterol"))
    bloodlevel = models.IntegerField(_("Blood level (Hemoglobin)"))
    weight = models.CharField(_("Weight"), max_length=50)
    created_at = models.DateTimeField(_("Created At"), auto_now=True)


class Glucose(models.Model):
    username = models.CharField(_("phonenumber"), max_length=50)
    glucose = models.CharField(_("Glucose"), max_length=50)
    date = models.CharField(_("Date"), max_length=50)
    time = models.CharField(_("Time"), max_length=50)
    created_at = models.DateTimeField(_("Created At"), auto_now=True)


class Pressure(models.Model):
    username = models.CharField(_("phonenumber"), max_length=50)
    systolic = models.IntegerField(_("Systolic"))
    diastolic = models.IntegerField(_("Diastolic"))
    date = models.CharField(_("Date"), max_length=50)
    time = models.CharField(_("Time"), max_length=50)
    created_at = models.DateTimeField(_("Created At"), auto_now=True)

class Weight(models.Model):
    username = models.CharField(_("phonenumber"), max_length=50)
    weight = models.CharField(_("Weight"), max_length=50)
    date = models.CharField(_("Date"), max_length=50)
    time = models.CharField(_("Time"), max_length=50)
    created_at = models.DateTimeField(_("Created At"), auto_now=True)


class PressureDataResult(models.Model):
    pressure_results = (
        ('select option', 'select option'),
        ('weekly', 'weekly'),
        ('monthly', 'monthly'),
    )
    sms_status = (
        ('select option', 'select option'),
        ('processing', 'processing'),
        ('delivered', 'delivered'),
        ('sent', 'sent'),
        ('failed', 'failed'),
    )
    phonenumber = models.CharField(_("Phone Number"), max_length=50)
    result = models.CharField(_("Sysotlic / Diasytolic"), max_length=50)
    task_category = models.CharField(_("Category"), max_length=50, choices=pressure_results, default=pressure_results[0][0])
    sms_category = models.CharField(_("Sms Status"), max_length=50, choices=sms_status, default=sms_status[0][0])
    created_at = models.DateTimeField(_("Created At"), auto_now=True)