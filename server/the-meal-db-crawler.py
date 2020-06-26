#!/usr/bin/python

import requests
import json
import string

#r = requests.get('https://www.themealdb.com/api/json/v1/1/search.php?f=%s' % letter)
#f = open("recipes/%s.json" % letter, "w")
#f.write(r.text)
#f.close()
#data = json.loads(r.text)

for letter in list(string.ascii_lowercase):

    f = open("recipes/%s.json" % letter, "r")
    data = json.loads(f.read())

    if data['meals'] is None:
        continue

    for meal in data['meals']:
        if 'strMeal' in meal:
            target = {
                'title': meal['strMeal'],
                'description': 'This recipe was imported from https://www.themealdb.com: %s' % meal['strSource']
            }

            # Add instructions
            if meal['strInstructions'] is not None:
                target['steps'] = [meal['strInstructions']]
            if meal['strMealThumb'] is not None:
                target['image'] = meal['strMealThumb']

            # Set labels
            labels = []
            if meal['strArea'] is not None:
                labels.append(meal['strArea'])
            if meal['strCategory'] is not None:
                labels.append(meal['strCategory'])
            if meal['strTags'] is not None:
                for tag in meal['strTags'].split(','):
                    labels.append(tag)
            target['labels'] = labels

            # Add ingredients
            ingredients = []
            for i in range(1,100):
                iTag = 'strIngredient%d' % i
                mTag = 'strMeasure%d' % i

                if iTag in meal and meal[iTag] is not None and meal[iTag] is not '' and mTag in meal and meal[mTag] is not None and meal[mTag] is not '':
                    ingredient = meal[iTag]
                    measure = meal[mTag]
                    ingredient = {
                        "ingredient": {
                            "name": ingredient
                        },
                        "measure": measure
                    }
                    ingredients.append(ingredient)

                    #print('Add ingredient {} - {}\n'.format(ingredient, measure))
            target['ingredients'] = ingredients
            print(json.dumps(target, indent=4, sort_keys=True))

            # POST to backend
            adminID = '<ID>'

            # print(target)
            url = "http://foodweek-env.eba-qy49mda5.eu-central-1.elasticbeanstalk.com/recipes"
            header = {
                "content-type": "application/json",
                "user": adminID
                }

            #Performs a POST on the specified url to get the service ticket
            response= requests.post(url,data=json.dumps(target), headers=header)
            
            print('Posted Recipe: %s' % response.status_code)
            if response.status_code == 500:
                print(response.text)
                exit(0)


        
