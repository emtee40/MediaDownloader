# MediaDownloader

MediaDownloader is a cross-platform java app which allows you to download files from YouTube, Facebook, Instagram, StreamCloud, SoundCloud and much more sites (way more will be added in the future).

There is also a Regular-Expression AddOn which allows you to search the website for example: URLs of any type.

Check out an online version of my YouTube Downloader (also unlocks GEMA blocked videos in Germany): http://youtube.r3d-soft.de

You have a favourite song on SoundClound and there is no download button or source where you can buy it?

Just check out my online version of the SoundCloud Downloader: http://soundcloud.r3d-soft.de

## Upcoming version (1.1b)
The upcoming version is 1.1b (Fresh-UI) which will include new features as well as a few tweaks and improvements. Here is a list:

New features:

* Added the option to save and load a DLC (Note: These DLCs are not equivalent to JDownloaders DLCs!! They are a simple plain text file separated per new-line)
* You are now able to delete a url from the table by pressing DEL on your keyboard
* A version checker is included, this features checks if your version is still up-to-date by checking the stable(!) release on http://download.r3d-soft.de (There is no beta release channel until now!)

Fixed issues:

* Fixed issues with Instagram (since they changed their API a bit)
* Fixed issues with MixCloud (since they changed their website)

## Current stable(!) version
Currently there Version 1.0b (Fresh-UI) available for download at: http://download.r3d-soft.de

This version was released on December 26th, 2015

# Usage (version 1.0b):
The usage is pretty simple. Just start the program. A GUI will pop up.

Make sure the settings on the bottom fits your needs just as the ffmpeg path (will be correct if you download the .rar file from my website) - Remove GEMA will allow people in Germany to download GEMA blocked videos/songs (any other settings I guess are self-explanatory - if not mail me at: admin[at]r3d-soft[dot]de)

And now you can start pasting your url you want to download from. For example https://www.youtube.com/watch?v=_mVW8tgGY_w

Just copy that url where it says "Download URL:"

After that click the button "Add to list" the URL will be analyze and then it will be added to the download queue

You can paste as many links in it as you can. When you're ready to download just hit the download button: "Download all!"

####Whats supported to download?

#####YouTube

Download videos and convert them into mp3 using ffmpeg (make sure it is installed under Linux!)

#####Instagram

Crawl profiles

#####Facebook

Crawl albums files

#####SoundCloud

Download mp3 files, even if official download is disabled!

#####Vimeo

Download videos and convert them into mp3 using ffmpeg (make sure it is installed under Linux!)

#####Shared.SX

Download videos/movies

#####StreamCloud.EU

Download videos/movies

#####NowVideo

Download video/movies

#####Vine

Crawls complete vine profiles

#####REExplorer

Allows you to search a website using regular-expressions - this allows you to download almost from every website (It is only recommended to advanced users who have a understand of regular expressions)

## Shortcuts (version 1.0b):
#####YouTube-URL:
* enter https://www.youtube.com/{USERNAME} to crawl all videos available on this channel

* enter https://www.youtube.com/watch?v={VIDEOID} to download a single video

* enter https://www.youtube.com/watch?v={VIDEOID}&list{PLAYLISTID} to download a complete playlist (YouTubes new endless playlists are supported but it downloads only the first 25-30 videos after that the playlist will be re-generated 

#####Facebook-URL:
*Following URLs are accepted:*

* https://www.facebook.com/{username} or

* https://www.facebook.com/{username}/photos_stream?tab=photos_albums or

* https://www.facebook.com/media/set/?set=a.{randomnumbers}.{randomnumbers}.{randomnumbers}&type=X

#####Instagram:
* enter http://instagram.com/{USERNAME} eg. http://instagram.com/therealgrimmie

* enter http://instagram.com/{...} link to a __single__ picture

# Donate:
If you like my work you can donate on my website __http://r3d-soft.de__
