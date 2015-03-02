# Cloud-Assignment-1-2

This project presents tweet clusters, markers,  heat map, and overall sentiment of tweets on Google map that is being updated in near real time. We used simple queue service to create a processing queue  for tweets that are delivered by the twitter streaming API. Then we defined a worker pool to read tweets from the queue and get sentiments of that tweet. each time we finish processing one twit，used SNS to notify the http servlet that has subscribed to the topic created before. Then we used web socket to update the website.
