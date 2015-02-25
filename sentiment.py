'''
Created on Nov 21, 2014

@author: Chang
'''

from __future__ import print_function
from alchemyapi import AlchemyAPI

text = ''

if __name__ == '__main__':
    import sys
    comLen = len(sys.argv) 
    if comLen == 2 and sys.argv[1]:
        path = sys.argv[1]
        try:
            f = open(path, "r")
            text = f.read().strip()

            if text == '':
                # The text file shouldn't be blank
                print(
                    'The file contains no text!')                
                sys.exit(0)
            # Close file
            f.close()
            
            alchemyapi = AlchemyAPI()
            response = alchemyapi.sentiment('text',text)
            print('type: ', response['docSentiment']['type'])
            if 'score' in response['docSentiment']:
                print('score: ', response['docSentiment']['score'])
            else:
                print('Error in sentiment analysis call: ', response['statusInfo'])

        except IOError:
            # The file doesn't exist.
            print(
                'Can not find text!')

            sys.exit(0)
        except Exception as e:
            print(e)
    elif comLen == 3 and sys.argv[1] == '-t':
        text = sys.argv[2]
        
        alchemyapi = AlchemyAPI()
        response = alchemyapi.sentiment('text',text)
        print('type: ', response['docSentiment']['type'])
        if 'score' in response['docSentiment']:
            print('score: ', response['docSentiment']['score'])
        else:
            print('Error in sentiment analysis call: ', response['statusInfo'])
    else:
        print('Not a valid command, please try again!')

