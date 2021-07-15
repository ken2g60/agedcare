from django.db import models
from django.utils.translation import gettext as _


# Create your models here.
class Deposit(models.Model):
    momo = models.CharField(_("Mobile Money"), max_length=50)
    amount = models.CharField(_("Amount"), max_length=50)
    txtRef = models.CharField(_("Reference"), max_length=50, null=True, blank=True)
    created_at = models.DateTimeField(_("Created At"), auto_now=True)

class TransactionHistory(models.Model):
    momo = models.CharField(_("Mobile Money"), max_length=50)
    amount = models.CharField(_("Amount"), max_length=50)
    txtRef = models.CharField(_("Reference"), max_length=50, null=True, blank=True)
    created_at = models.DateTimeField(_("Created At"), auto_now=True)


class SubscriptionHistory(models.Model):
    first_name = models.CharField(_("First Name"), max_length=50, null=True, blank=True)
    last_name = models.CharField(_("Last Name"), max_length=50, null=True, blank=True)
    phonenumber = models.CharField(_("Phone Number"), max_length=50, null=True, blank=True)
    subscription = models.CharField(_("Subscription Type"), max_length=50)
    paid_status = models.BooleanField(_("Payment Status"), default=False)
    amount = models.CharField(_("Amount"), max_length=50)
    created_at = models.DateTimeField(_("Created At"), auto_now_add=True)
