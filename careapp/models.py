from django.db import models
from django.utils.translation import gettext as _
from django.db.models import signals
from django.dispatch import receiver

from django.db.models.signals import pre_save
from utils.helpers import random_generate 


class UserModel(models.Model):
    userId = models.CharField(_("User ID"), max_length=50)
    session_phonenumber = models.CharField(_("Session Phone Number"), max_length=50)
    fullname = models.CharField(_("Full Name"), max_length=50)
    phonenumber = models.CharField(_("Phone Number"), max_length=50)
    gender = models.CharField(_("Gender"), max_length=50)
    balance = models.DecimalField(default=0, max_digits=12, decimal_places=2)
    created_at = models.DateTimeField(_("Created At"), auto_now_add=True)
    
    
    
    def __str__(self):
        return self.fullname
    
    
class PersonalDetail(models.Model):

    gender_options = (
        ('Select an option', 'Select an option'),
        ('Male', 'Male'),
        ('Female', 'Female')
    )
    lanuage_options = (
        ('Select an option', 'Select an option'),
        ('First Lanauage', 'First Lanuage'),
        ('Second Lanauage', 'Second Lanauage')
    )
    sector_options = (
        ('Select an option', 'Select an option'),
        ('Public Sector', 'Public Sector'),
        ('Private Sector', 'Private Sector')
    )

    pension_options = (
        ('Select an option', 'Select an option'),
        ('Social Security', 'Social Security'),
        ('Other', 'Other')
    )
    date_of_birth = models.CharField(_("Date of birth"), max_length=50)
    height = models.CharField(_("Height"), max_length=50)
    occupation = models.CharField(_("Occupation"), max_length=50)
    sector = models.CharField(_("Sector"), max_length=50, choices=sector_options, default=sector_options[0][0])
    region = models.CharField(_("Region"), max_length=250)
    municipality_district = models.CharField(_("Municipality / District"), max_length=50)
    town = models.CharField(_("Town"), max_length=50)
    residential_address = models.CharField(_("Residential Address"), max_length=50)
    number_of_years_service = models.CharField(_("Number of years in service"), max_length=50)
    number_dependents = models.CharField(_("Number Dependents"), max_length=50)
    registered_nhis =  models.BooleanField(_("Registered with NHIS"), default=False)
    private_health = models.BooleanField(_("Registered with any private health insurance?"), default=False)
    pension_scheme = models.CharField(_("Pension Scheme"), max_length=50, choices=pension_options)
    user = models.OneToOneField(UserModel, related_name='personal', on_delete=models.CASCADE)
    created_at = models.DateTimeField(_("Created At"), auto_now=True)
    
    
    def __str__(self):
        return self.date_of_birth
    
    
    def model_callable(self):
        return self.user

class ContributionModel(models.Model):
    userId = models.CharField(_("User ID"), max_length=50)
    momo = models.CharField(_("Mobile Money"), max_length=50)
    amount = models.CharField(_("Amount"), max_length=50)
    # system get month paid
    month = models.CharField(_("Month"), max_length=50, null=True, blank=True)
    created_at = models.DateTimeField(_("Created At"), auto_now=True)


    
    
def generate_user_id(instance, sender, *args, **kwargs):
    if not instance.userId:
        instance.userId = random_generate(4)
pre_save.connect(generate_user_id, sender=UserModel)