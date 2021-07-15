from django.contrib import admin

# Register your models here.
from .models import Deposit, TransactionHistory

admin.site.register(Deposit)
admin.site.register(TransactionHistory)