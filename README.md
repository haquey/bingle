# Bingle

Bingle is a robust search engine application designed for students and instructors. In addition to being able to search files by content, title and tags, Bingle also comes equipped
with a fully functional discussion board, marketplace and webcrawler.

## Table of content

- [Installation](#markdown-header-installation)
- [API and Libraries](#markdown-header-apilibraries)   
- [Search](#markdown-header-search)
- [Upload and Index](#markdown-header-upload-index) 
- [Discussion board](#markdown-header-discussion-board) 
- [Marketplace](#markdown-header-marketplace) 
- [Crawler](#markdown-header-crawler) 
- [Profile page](#markdown-header-profile-page)
- [Contributors](#markdown-header-contributors)
- [Leaderboard](#markdown-header-leaderboard)

## Installation
1. Clone the bingle repository.
2. Install maven. Follow the instructions given here- https://maven.apache.org/install.html
3. Set up mongoDB database
    * Open the file **/bingle-web-api/src/main/resources/application.properties**
    * If you are using mlab or similar mongoDB service, change **spring.data.mongodb.uri** variable to your MongoDB URI.
    * If you have a local instance of monogDB running, then change the **spring.data.mongodb.databse** and **spring.data.mongodb.port** to your local mongoDB name and port.
    * For further information on how to install and run mongoDB on local, visit- https://docs.mongodb.com/manual/administration/install-community/
4. Go to **/bingle-web-api/** and run the command **mvn clean install**. After the application is
done building, launch it with the command **mvn spring-boot:run**.
5. Go to localhost:8080 on your web browser. It is strongly recommended that you use chrome as your
browser, for the full bingle experience.

## API/Libraries
`SpringBoot:` Spring boot is a webframework that is used to kickstart the Bingle application. Spring boot makes connecting<br/>
frontend, backend and the database together simple and organized.

`MongoDB:` MongoDB is a open source No-SQL database. Since a search engine application reads data much more than it writes<br/>
it is optimal to use a no-sql database.

`BootStrap:` Provides a baseline design to create the frontend UI.

`Apache Lucene:` Provides tools to implement indexing and searching.

`Apache Tika:` Used to parse and extract text from PDF and Doc files.

`Google Cloud Vision:` Used for image recognition and automatic tagging.

`Mockito:` Used for mocking object during testing.



## Search
Bingle is capable of searching text, html, image and pdf files by content, title and tags.
You can specify the field you want search by
or use the "All" option which searches by content, title and tags at the same time. 

![Search Option](/Images/search-page1.PNG?raw=true "Search option")

The result entries also show a snippet/the context of the content where the searched keywords were found.

![Search Option](/Images/search-img2.PNG?raw=true "Search option 2")

## Upload-Index
With bingle you can also upload and index html, text, image, doc and pdf files. In order to upload and index a file
you have to register/log in and click **Upload** on the navigation bar.

![Nav Bar](/Images/nav-img1.PNG?raw=true "nav bar")

After you navigate to the upload page, you choose the file you want to upload and give it a title.
Bingle also allows you to mark your files with key tags. To tag a file, simply add your tags on the Tags field
in the upload form and separate each tag with a ';' (semi-colon). Lastly, click the **Upload** button to upload and index the file.

![Upload](/Images/upload-img1.PNG?raw=true "upload 1r")

## Discussion Board
Bingle also comes with a Discussion board, where students and instructors can ask and answer questions.
To access discussion board, click "Discussion" on the navigation bar and it will take you to the discussion board.

![Nav Bar](/Images/nav-img2.PNG?raw=true "nav 2")

Once you are there, you can click on the thread entries to see their respective posts and responses. You can also upvote/downvote threads.

![Discussion](/Images/discussion-img1.PNG?raw=true "disc 1")

To make a post on a thread (you must be logged in), type the content of your post on the Post field and click the
**Make New Post** button. Additonally, Bingle allows you to type your post in Markdown and Latex. You must wrap Latex commands
between a pair of "$$" characters for optimal parsing.

![Discussion](/Images/discussion-img2.PNG?raw=true "disc 2")

Since Bingle is first and foremost a search engine, it also allows you to search discussion board posts by content, title, users and tags.

![Discussion](/Images/discussion-img3.PNG?raw=true "disc 3")

## Marketplace
Bingle is also comes packed with a marketplace where users can buy and sell items. To go to the marketplace, click the marketplace button
on the Navigation bar.

![Nav Bar](/Images/nav-img3.PNG?raw=true "nav 4")

To view an item, simply click on the item entry and it will show contact information about purchasing the item.

![Marketplace](/Images/marketplace-img2.PNG?raw=true "market 1")

To sell an item, click on the **Make Listing** button, put the appropriate information on the listing form and click submit.

![Marketplace](/Images/marketplace-img3.PNG "market 2")

Maketplace listings are also searchable just like the discussion board.

## Crawler
Bingle also has a fully functioning built-in webcrawler. In order to navigate to the crawler, click `Crawler` on the nav bar.

![Nav Bar](/Images/nav-img4.PNG?raw=true "nav 4")

On the crawler page, fill in the crawler form with the URL that you want to crawl, depth and maximum number of pages. If the file option is
selected, the crawler will only index pdf files and if the image option is chosen, then it will index all types of image files.
Once all the fields have been filled, click `Crawl` to let the crawling begin.

![Crawler](/Images/crawl-img1.PNG?raw=true "crawl 1")

Using a factory can be convenient to integrate crawler4j in a IoC environement (like Spring, Guice) 
or to pass information or a collaborator to each `WebCrawler` instance.


## Profile page
Bingle also has a detailed profile page for every user that keeps record of all the files, listings and discussion posts a user uploaded
or posted. To check profile page, click on the username in navigation bar(you must be logged in).

![Nav Bar](/Images/nav-img5.PNG?raw=true "nav 5")

Additionally, Bingle analyzes user data and creates breakdown charts and statistics about files uploaded, tags used, and files searched (this happens
for both individual data and site wide aggregated data, accessible via the leaderboard page).
Users can also add an image avatar for their profile.

![Profile](/Images/profile-img1.PNG?raw=true "profile 1")

## Leaderboard
Bingle awards points to students if his/her discussion board threads/posts gets upvoted by another user. It also rewards points for files uploaded.
Users with the highest points are shown on the bingle leaderboard. To access the leaderboard, click on `Leaderboard` on the navigation bar.

![Leaderboard](/Images/leaderboard-img1.PNG?raw=true "Leaderboard1")

The leaderboard also shows global statistics.

![Leaderboard](/Images/leaderboard-img2.PNG?raw=true "Leaderboard2")

## Contributors
1. Kareem Mohamed
2. Yasir Haque
3. Sol Han
4. Marion Vasantharajah
5. Sadman Rafid

### This is a mirror of a private repo on BitBucket
