from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import CustomUser, Subscription, Payment
# Register your models here.

class CustomUserAdmin(admin.ModelAdmin):
     list_display = ('userId','phonenumber', 'email', 'balance', 'is_active', 'is_staff', 'is_superuser', 'created_at')


 
class SubscriptionAdmin(admin.ModelAdmin):
     list_display = ('phonenumber', 'start_date', 'end_date','package_type', 'subscription_type','is_active', 'created_at')


class PaymentAdmin(admin.ModelAdmin):
     list_display = ('first_name', 'last_name', 'phonenumber', 'subscription', 'paid_status', 'paid_date', 'amount', 'terminated_on', 'email', 'created_at')    


admin.site.register(CustomUser, CustomUserAdmin)
admin.site.register(Subscription, SubscriptionAdmin)
admin.site.register(Payment, PaymentAdmin)