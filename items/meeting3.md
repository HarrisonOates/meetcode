# Zenith Labs
## Team Meeting 3 - Week 8 - 27th April 2023 (1:15pm - 2:15pm)
**Absent:**
<br>
**Lead/scribe:** Jayden

## Agenda Items
| Number |               Item |
|:-------|-------------------:|
| 1      |  Checkpoint Update |
| 2      | Individual Updates |
| 3      |  Questions for app |
| 4      |    Assigning Tasks |

## Meeting Minutes
Checkpoint
- Geun is doing it straight after the meeting
  - Have a wireframe completed
  - Have a parser and tokenizer for search
  - Have firebase setup for use
  - Have AVLTree as our data structure with most functionality
  - Have started the UI
  - Have started the tokenizer for syntax highlighting
  - Have started the messaging code
  - Have the main ideas for the project sorted out,
  - Have developed some ideas for how things will integrate together

Schedule
- Firebase - setup by 24th
- Parser, Grammar and tokenizer for search - completed by 27th
- Wireframe - completed by 27th
- AVL Tree - completed and tested by 27th
- UI - completed by 30th
- Messaging - completed by 30th
- Syntax Highlighting - completed by 30th
- Voicing new features - must be done by 5th
- Create questions for the app - completed by 5th
- Storing data in a searchable format - completed by 5th
- Implementing UI functions - completed by 5th
- Substantial sections of the code tested - completed by the 11th
- Substantial sections of the report - completed by the 11th
- App is functional - completed by 11th

Roles
- Alex - Messaging and Database
- Geun - Data Structures and User Logins
- Harry - Syntax Highlighting and User Settings
- Nikhila - User Interface and User Experience
- Jayden - Search and Notifications
- Everyone - Testing, question writing and report writing

Questions for the app
- 1 category each
- Minimum of 5 questions per person
- Topics:
  - Data Structures - Geun
  - Recursion - Jayden
  - Algorithms - Harry
  - Control Flow - Nikhila
  - Miscellaneous - Alex
- Using UI Nikhila has mocked up to format questions

Features to voice
- Korean translation
- Syntax highlighting
- Points system

Need to implement logout
Need to figure out how to test with android studio and firebase
Looks into whether you can lock the firebase, preventing read and writes at the same time
Harry to additionally take up User Settings

Individual Updates
- Alex
  - Messaging code stored in Message and MessageThread
  - Messaging code works - Send messages to arbitrary used, is stored, and can be responded to
  - Potentially may be able to like messages too - need to test
  - Firebase code is written - Should be able to read and write to different objects in firebase
- Geun
  - AVL Tree is functional (insertion, deletion), just needs to be tested (have done some testing)
  - User logins - passwords are salted
- Harry
  - Setting up tokenizer and parser for syntax highlighting
  - tokenizer is pretty much done, may have further refactoring
  - Started on the parser, about halfway through
  - Looking into the 2,500 data instances requirement
- Nikhila
  - Wireframes are all done - Home page, Categories page, Individual Category page, Messages page 
  - Getting shadows working - Making layout responsive, adjusting to different sized screens
  - Implementing the User Interface
- Jayden
  - Completed tokenizer for search, splitting things depending on the type of search query
  - Completed parser for search results, returning an array of words, separated by each individual query
  - Queries can be Questions, Topics, Posts, Users
  - Looking into how to implement search with the stored data

## TODO Items
| Task                    | Assignee |
|:------------------------|---------:|
| User settings           |    Harry |
| Voicing features        |     Alex |
| Week 8 Checkpoint       |     Geun |
| Finish AVL Tree Testing |     Geun |
| Finish UI | Nikhila |
| Syntax Highlighting | Harry |
| Messaging Code | Alex |
| Create data structures for search | Jayden |
| Processing searches | Jayden |
| Writing questions for app | All |

## Scribe Rotation for next meetings
|  Name   |
|:-------:|
|  Geun   |
| Nikhila |
|  Harry  |
|  Alex   |
| Jayden  |