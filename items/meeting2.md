# Zenith Labs
## Team Meeting 2 - Week 7 - 20th April 2023 (6:10pm - 8:05pm)
**Absent:** Harrison

**Lead/scribe:** Alex

## Agenda Items
| Number |                Item |
|:-------|--------------------:|
| 1      |       Project scope |
| 2      |   High level design |
| 3      | Assignment of tasks |
| 4      |        Next meeting |

## Meeting Minutes
Project scope (basic features):
 - Log in but not sign up, we'll just have builtin accounts for users
 - For the 2,500 data points, we'll just save all interacts we have. This should be done in the opposite file format (e.g. XML if the rest uses JSON)
 - Search one search bar for everything: posts on a forum, people (actual name and their username), questions. Would have a single search thing that can be filtered or using tokens, etc. to refine
 - Search: prioritise the order (e.g. people before posts, titles before descriptions). 
 - Search should be fuzzy
 - Search grammar:
   - quotation marks, AND, OR -> expression
   - expressions can start with ? (for questions), @ (for people), ! (for in posts), #<category> (e.g. #algos, #avltree)
 - Tree data structure
   - Tree will store past questions locally (+ save to disk) for faster searching
   - Blocked user tree (??)
   - Tree type can be decided by whoever needs to do it (must be AVL or RB)
 - JSON for main data storage (except playback of 2500 data points -> XML)

Project scope (general features):
 - Have 'learn to code questions' - will show code in the top half (syntax highlighted), and it will show a question, and then a short answer input
 - Post on questions, direct messaging, liking comments / questions
 - User blocking 
 - Firebase auth.
 - Firebase persistence
 - Notifications for new questions
 - Anon. posts -> could just hardcode to a post under an anon. user
 - Have category of questions in chronological order, can filter though in search
 - Can also like / post under each category of questions
 - Replying to messages
 - Points system per user stored locally
   - (graph it over time if you have time)
 - A 45 minute discussion brings you the next minute:
   - First page has categories and the question of the day
   - Clicking on a category takes you to a list of questions
   - Clicking on question takes you to a question page
   - Question page should have code, description and an answer box
   - Get 3 points for correct answer
   - If incorrect answer you get a second chance (if you get it right the second time you get 1 point)
   - You can only see comments after you answer once
   - Comments are done like Facebook/Instagram - flat but with replies
 - 

Features to voice:
 - Syntax highlighting
 - Language settings (English / Korean)
 - Having a use for points

High level design and task allocation:
 - Facade to hide the ugliness of the Firebase backend
 - Subsystems:
   - Search (Jayden)
   - Authorisation / hashing (Geun)
   - Syntax highlighting (Harrison)
   - UI (Nikhila)
   - Messaging (Alex)
     - question.sendMessage()
   - API calls to Firebase (Alex)
   - Classes for People, Comments, Questions (Jayden)
     - and their conversions to and from JSON
   - Data playback (Harrison)
   - Notifications (Jayden)
   - Facade (Nikhila)
   - Caching of old data via trees (Geun)
 - Libraries:
   - Fuzzy search, XML, JSON, hashing

## TODO Items
| Task                                                  | Assignee |
|:------------------------------------------------------|---------:|
| Create UML for this by Saturday                       |     Alex |
| Convert diagrams to a more readable format for report |     Alex |
| Lab checkpoint                                        |     Geun |
 | Wireframes for UI look into Android Studio & APK     |  Nikhila |
 | Write substantial portions of code                   |      All |
 | Setting up Firebase                                  | Harrison |
 | Grammar for the search                               |   Jayden |
 | Grammar for the syntax highlighting                  | Harrison |
| Create a whenisgood for next week                     |   Jayden |

