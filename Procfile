web: gunicorn  agedcare.wsgi:application --log-file -
worker: celery -A agedcare worker -B --loglevel=info