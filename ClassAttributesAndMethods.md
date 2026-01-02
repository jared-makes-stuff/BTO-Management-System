# Class Attributes and Methods

## Entity Classes

### User
| Attribute | Type | Description |
|-----------|------|-------------|
| name | String | User's name |
| nric | String | User's NRIC (ID) |
| age | int | User's age |
| password | String | User's password |
| maritalStatus | MarriageStatusEnum | User's marital status |
| filter | Filter | User's filter preferences |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| User() | | | Default constructor |
| User() | String name, String nric, int age, MarriageStatusEnum maritalStatus, String password, Filter filter | | Full constructor |
| setName() | String name | void | Sets user's name |
| setNric() | String nric | void | Sets user's NRIC |
| setAge() | int age | void | Sets user's age |
| setPassword() | String password | void | Sets user's password |
| setMaritalStatus() | MarriageStatusEnum maritalStatus | void | Sets user's marital status |
| setFilter() | Filter filter | void | Sets user's filter |
| getName() | | String | Gets user's name |
| getNric() | | String | Gets user's NRIC |
| getAge() | | int | Gets user's age |
| getPassword() | | String | Gets user's password |
| getMaritalStatus() | | MarriageStatusEnum | Gets user's marital status |
| getFilter() | | Filter | Gets user's filter |
| authenticate() | String password | boolean | Authenticates user with given password |
| toString() | | String | Returns string representation of user |

### Applicant
| Attribute | Type | Description |
|-----------|------|-------------|
| application | ArrayList<BTOApplication> | Applications submitted by this applicant |
| enquiries | ArrayList<Enquiry> | Enquiries submitted by this applicant |
| database (static) | ApplicantDatabase | Database of all applicants |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| Applicant() | | | Default constructor |
| Applicant() | String name, String nric, int age, MarriageStatusEnum maritalStatus, String password, Filter filter, BTOApplication application, ArrayList<Enquiry> enquiries | | Full constructor |
| setApplication() | BTOApplication application | void | Sets applicant's application |
| setEnquiries() | ArrayList<Enquiry> enquiries | void | Sets applicant's enquiries |
| setDatabase() (static) | ApplicantDatabase database | void | Sets the applicant database |
| getApplication() | | ArrayList<BTOApplication> | Gets applicant's applications |
| getEnquiries() | | ArrayList<Enquiry> | Gets applicant's enquiries |
| getDatabase() (static) | | ApplicantDatabase | Gets the applicant database |
| addApplication() | BTOApplication application | void | Adds an application to applicant's list |
| addEnquiry() | Enquiry enquiry | void | Adds an enquiry to applicant's list |
| findApplicantByName() (static) | String name | Applicant | Finds applicant by name |
| findApplicantByNRIC() (static) | String nric | Applicant | Finds applicant by NRIC |
| findApplicantsByAge() (static) | int age | ArrayList<Applicant> | Finds applicants by age |
| findApplicantByApplication() (static) | BTOApplication application | Applicant | Finds applicant by application |
| findApplicantByEnquiry() (static) | Enquiry enquiry | Applicant | Finds applicant by enquiry |
| findApplicantsByMaritalStatus() (static) | MarriageStatusEnum status | ArrayList<Applicant> | Finds applicants by marital status |
| addToDatabase() (static) | Applicant applicant | boolean | Adds applicant to database |
| removeFromDatabase() (static) | Applicant applicant | boolean | Removes applicant from database |
| getAllApplicants() (static) | | ArrayList<Applicant> | Gets all applicants in database |
| toString() | | String | Returns string representation of applicant |

### Officer
| Attribute | Type | Description |
|-----------|------|-------------|
| assignedProject | Project | Officer's assigned project |
| officerDatabase (static) | OfficerDatabase | Database of all officers |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| Officer() | | | Default constructor |
| Officer() | String name, String nric, int age, String password, MarriageStatusEnum maritalStatus, Filter filter, BTOApplication applications, ArrayList<Enquiry> enquiries, ArrayList<Project> assignedProjects | | Full constructor |
| setAssignedProject() | Project assignedProject | void | Sets officer's assigned project |
| setDatabase() (static) | OfficerDatabase database | void | Sets the officer database |
| getOfficerDatabase() | | OfficerDatabase | Gets the officer database |
| getAssignedProject() | | Project | Gets officer's assigned project |
| getDatabase() (static) | | OfficerDatabase | Gets the static officer database |
| assignToProject() | Project project | boolean | Assigns officer to a project |
| unassignFromProject() | Project project | boolean | Unassigns officer from a project |
| isAssignedToProject() | Project project | boolean | Checks if officer is assigned to a project |
| findOfficerByName() (static) | String name | Officer | Finds officer by name |
| findOfficerByNRIC() (static) | String nric | Officer | Finds officer by NRIC |
| findOfficersByAge() (static) | int age | ArrayList<Officer> | Finds officers by age |
| findOfficerByApplication() (static) | BTOApplication application | Officer | Finds officer by application |
| findOfficerByEnquiry() (static) | Enquiry enquiry | Officer | Finds officer by enquiry |
| findOfficersByProject() (static) | Project project | ArrayList<Officer> | Finds officers by project |
| findOfficerByMaritalStatus() (static) | MarriageStatusEnum status | ArrayList<Officer> | Finds officers by marital status |
| addToDatabase() (static) | Officer officer | boolean | Adds officer to database |
| removeFromDatabase() (static) | Officer officer | boolean | Removes officer from database |
| getAllOfficers() (static) | | ArrayList<Officer> | Gets all officers in database |
| toString() | | String | Returns string representation of officer |

### Manager
| Attribute | Type | Description |
|-----------|------|-------------|
| managedProjects | ArrayList<Project> | Projects managed by this manager |
| database (static) | ManagerDatabase | Database of all managers |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| Manager() | | | Default constructor |
| Manager() | String name, String nric, int age, String password, MarriageStatusEnum maritalStatus, Filter filter, ArrayList<Project> managedProjects | | Full constructor |
| setManagedProjects() | ArrayList<Project> managedProjects | void | Sets manager's managed projects |
| setDatabase() (static) | ManagerDatabase database | void | Sets the manager database |
| getManagedProjects() | | ArrayList<Project> | Gets manager's managed projects |
| getDatabase() (static) | | ManagerDatabase | Gets the manager database |
| addProject() | Project project | void | Adds a project to manager's list |
| removeProject() | Project project | boolean | Removes a project from manager's list |
| findManagerByName() (static) | String name | Manager | Finds manager by name |
| findManagerByNRIC() (static) | String nric | Manager | Finds manager by NRIC |
| findManagersByAge() (static) | int age | ArrayList<Manager> | Finds managers by age |
| findManagersByProject() (static) | Project project | ArrayList<Manager> | Finds managers by project |
| findManagerByMaritalStatus() (static) | MarriageStatusEnum status | ArrayList<Manager> | Finds managers by marital status |
| addToDatabase() (static) | Manager manager | boolean | Adds manager to database |
| removeFromDatabase() (static) | Manager manager | boolean | Removes manager from database |
| getAllManagers() (static) | | ArrayList<Manager> | Gets all managers in database |
| toString() | | String | Returns string representation of manager |

### BTOApplication
| Attribute | Type | Description |
|-----------|------|-------------|
| applicationDate | LocalDate | Date of application |
| applicant | Applicant | Applicant who submitted the application |
| project | Project | Project applied for |
| flatType | FlatTypeEnum | Type of flat applied for |
| status | BTOApplicationStatusEnum | Status of application |
| withdrawalStatus | WithdrawalStatusEnum | Withdrawal status of application |
| database (static) | BTOApplicationDatabase | Database of all BTO applications |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| BTOApplication() | | | Default constructor |
| BTOApplication() | Applicant applicant, Project project, FlatTypeEnum flatType | | Constructor with essential parameters |
| setApplicationDate() | LocalDate applicationDate | void | Sets application date |
| setApplicant() | Applicant applicant | void | Sets applicant |
| setProject() | Project project | void | Sets project |
| setFlatType() | FlatTypeEnum flatType | void | Sets flat type |
| setStatus() | BTOApplicationStatusEnum status | void | Sets application status |
| setWithdrawalStatus() | WithdrawalStatusEnum withdrawalStatus | void | Sets withdrawal status |
| setDatabase() (static) | BTOApplicationDatabase database | void | Sets the application database |
| getApplicationDate() | | LocalDate | Gets application date |
| getApplicant() | | Applicant | Gets applicant |
| getProject() | | Project | Gets project |
| getFlatType() | | FlatTypeEnum | Gets flat type |
| getStatus() | | BTOApplicationStatusEnum | Gets application status |
| getWithdrawalStatus() | | WithdrawalStatusEnum | Gets withdrawal status |
| getDatabase() (static) | | BTOApplicationDatabase | Gets the application database |
| requestWithdrawal() | | boolean | Requests withdrawal of application |
| findApplicationsByDate() (static) | LocalDate date | ArrayList<BTOApplication> | Finds applications by date |
| findApplicationsByApplicant() (static) | Applicant applicant | ArrayList<BTOApplication> | Finds applications by applicant |
| findApplicationsByProject() (static) | Project project | ArrayList<BTOApplication> | Finds applications by project |
| getAllApplications() (static) | | ArrayList<BTOApplication> | Gets all applications in database |
| addToDatabase() (static) | BTOApplication application | boolean | Adds application to database |
| removeFromDatabase() (static) | BTOApplication application | boolean | Removes application from database |
| toString() | | String | Returns string representation of application |

### Booking
| Attribute | Type | Description |
|-----------|------|-------------|
| bookingDateTime | LocalDate | Date and time of booking |
| application | BTOApplication | Associated BTO application |
| processingOfficer | Officer | Officer processing the booking |
| flatType | FlatTypeEnum | Type of flat booked |
| status | BookingStatusEnum | Status of booking |
| database (static) | BookingDatabase | Database of all bookings |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| Booking() | | | Default constructor |
| Booking() | LocalDate bookingDateTime, BTOApplication application, Officer processingOfficer, FlatTypeEnum flatType, BookingStatusEnum status | | Full constructor |
| setBookingDateTime() | LocalDate bookingDateTime | void | Sets booking date and time |
| setApplication() | BTOApplication application | void | Sets associated application |
| setProcessingOfficer() | Officer processingOfficer | void | Sets processing officer |
| setFlatType() | FlatTypeEnum flatType | void | Sets flat type |
| setStatus() | BookingStatusEnum status | void | Sets booking status |
| setDatabase() (static) | BookingDatabase database | void | Sets the booking database |
| getBookingDateTime() | | LocalDate | Gets booking date and time |
| getApplication() | | BTOApplication | Gets associated application |
| getProcessingOfficer() | | Officer | Gets processing officer |
| getFlatType() | | FlatTypeEnum | Gets flat type |
| getStatus() | | BookingStatusEnum | Gets booking status |
| getDatabase() (static) | | BookingDatabase | Gets the booking database |
| generateReceipt() | | Receipt | Generates receipt for booking |
| cancelBooking() | | boolean | Cancels booking |
| confirmBooking() | | boolean | Confirms booking |
| findBookingByDate() (static) | LocalDate date | ArrayList<Booking> | Finds bookings by date |
| findBookingByApplication() (static) | BTOApplication application | Booking | Finds booking by application |
| findBookingsByOfficer() (static) | Officer officer | ArrayList<Booking> | Finds bookings by officer |
| getAllBookings() (static) | | ArrayList<Booking> | Gets all bookings in database |
| findBookingsByFlatType() (static) | FlatTypeEnum flatType | ArrayList<Booking> | Finds bookings by flat type |
| findBookingsByStatus() (static) | BookingStatusEnum status | ArrayList<Booking> | Finds bookings by status |
| addToDatabase() (static) | Booking booking | boolean | Adds booking to database |
| removeFromDatabase() (static) | Booking booking | boolean | Removes booking from database |
| toString() | | String | Returns string representation of booking |

### Enquiry
| Attribute | Type | Description |
|-----------|------|-------------|
| enquiryID | String | Unique ID for enquiry |
| dateTime | LocalDate | Date and time of enquiry |
| content | String | Content of enquiry |
| reply | String | Reply to enquiry |
| replyDate | LocalDate | Date of reply |
| submittedBy | Applicant | Applicant who submitted the enquiry |
| project | Project | Project the enquiry is about |
| status | EnquiryStatusEnum | Status of enquiry |
| respondent | Officer | Officer who responded to the enquiry |
| database (static) | EnquiryDatabase | Database of all enquiries |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| Enquiry() | | | Default constructor |
| Enquiry() | String enquiryID, LocalDate dateTime, String content, String reply, LocalDate replyDate, Applicant submittedBy, Project project, EnquiryStatusEnum status, Officer respondent | | Full constructor |
| setEnquiryID() | String enquiryID | void | Sets enquiry ID |
| setDateTime() | LocalDate dateTime | void | Sets date and time |
| setContent() | String content | void | Sets content |
| setReply() | String reply | void | Sets reply |
| setReplyDate() | LocalDate replyDate | void | Sets reply date |
| setSubmittedBy() | Applicant submittedBy | void | Sets submitter |
| setProject() | Project project | void | Sets project |
| setStatus() | EnquiryStatusEnum status | void | Sets status |
| setRespondent() | Officer respondent | void | Sets respondent |
| setDatabase() (static) | EnquiryDatabase database | void | Sets the enquiry database |
| getEnquiryID() | | String | Gets enquiry ID |
| getDateTime() | | LocalDate | Gets date and time |
| getContent() | | String | Gets content |
| getReply() | | String | Gets reply |
| getReplyDate() | | LocalDate | Gets reply date |
| getSubmittedBy() | | Applicant | Gets submitter |
| getProject() | | Project | Gets project |
| getStatus() | | EnquiryStatusEnum | Gets status |
| getRespondent() | | Officer | Gets respondent |
| getDatabase() (static) | | EnquiryDatabase | Gets the enquiry database |
| respond() | Officer officer, String response | boolean | Records a response to the enquiry |
| generateEnquiryID() (private) | | void | Generates a unique enquiry ID |
| findEnquiriesByEnquiryID() (static) | String enquiryID | Enquiry | Finds enquiry by ID |
| findEnquiriesBySubmittedDate() (static) | LocalDate date | ArrayList<Enquiry> | Finds enquiries by submitted date |
| findEnquiriesBySubmitter() (static) | Applicant submitter | ArrayList<Enquiry> | Finds enquiries by submitter |
| findEnquiriesByRespondent() (static) | Officer respondent | ArrayList<Enquiry> | Finds enquiries by respondent |
| findEnquiriesByReplyDate() (static) | LocalDate date | ArrayList<Enquiry> | Finds enquiries by reply date |
| findEnquiriesByProject() (static) | Project project | ArrayList<Enquiry> | Finds enquiries by project |
| getAllEnquiries() (static) | | ArrayList<Enquiry> | Gets all enquiries in database |
| addToDatabase() (static) | Enquiry enquiry | boolean | Adds enquiry to database |
| findEnquiriesByStatus() (static) | EnquiryStatusEnum status | ArrayList<Enquiry> | Finds enquiries by status |
| removeFromDatabase() (static) | Enquiry enquiry | boolean | Removes enquiry from database |
| toString() | | String | Returns string representation of enquiry |

### Filter
| Attribute | Type | Description |
|-----------|------|-------------|
| projectName | String | Project name filter |
| neighborhoodList | ArrayList<String> | List of neighborhoods to filter by |
| minPrice | double | Minimum price filter |
| maxPrice | double | Maximum price filter |
| flatTypes | ArrayList<FlatTypeEnum> | List of flat types to filter by |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| Filter() | | | Default constructor |
| Filter() | String projectName, ArrayList<String> neighborhoodList, double minPrice, double maxPrice, ArrayList<FlatTypeEnum> flatTypes | | Full constructor |
| setProjectName() | String projectName | void | Sets project name filter |
| setNeighborhoodList() | ArrayList<String> neighborhoodList | void | Sets neighborhood list filter |
| setMinPrice() | double minPrice | void | Sets minimum price filter |
| setMaxPrice() | double maxPrice | void | Sets maximum price filter |
| setFlatTypes() | ArrayList<FlatTypeEnum> flatTypes | void | Sets flat types filter |
| getProjectName() | | String | Gets project name filter |
| getNeighborhoodList() | | ArrayList<String> | Gets neighborhood list filter |
| getMinPrice() | | double | Gets minimum price filter |
| getMaxPrice() | | double | Gets maximum price filter |
| getFlatTypes() | | ArrayList<FlatTypeEnum> | Gets flat types filter |
| setPriceRange() | double minPrice, double maxPrice | void | Sets price range filter |
| addFlatType() | FlatTypeEnum flatType | void | Adds flat type to filter |
| addNeighborhood() | String neighborhood | void | Adds neighborhood to filter |
| matchesProject() | Project project | boolean | Checks if a project matches the filter criteria |
| toString() | | String | Returns string representation of filter |

### FlatType
| Attribute | Type | Description |
|-----------|------|-------------|
| numUnits | int | Total number of units |
| availableUnits | int | Number of available units |
| sellingPrice | double | Selling price of units |
| type | FlatTypeEnum | Type of flat |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| FlatType() | | | Default constructor |
| FlatType() | int numUnits, int availableUnits, double sellingPrice, FlatTypeEnum type | | Full constructor |
| setNumUnits() | int numUnits | void | Sets number of units |
| setAvailableUnits() | int availableUnits | void | Sets number of available units |
| setSellingPrice() | double sellingPrice | void | Sets selling price |
| setType() | FlatTypeEnum type | void | Sets flat type |
| getType() | | FlatTypeEnum | Gets flat type |
| getNumUnits() | | int | Gets number of units |
| getAvailableUnits() | | int | Gets number of available units |
| getSellingPrice() | | double | Gets selling price |
| decreaseAvailableUnits() | | boolean | Decreases available units by 1 |
| increaseAvailableUnits() | | boolean | Increases available units by 1 |
| toString() | | String | Returns string representation of flat type |

### OfficerApplication
| Attribute | Type | Description |
|-----------|------|-------------|
| applicationDate | LocalDate | Date of application |
| officer | Officer | Officer applying |
| project | Project | Project applied for |
| status | OfficerApplicationStatusEnum | Status of application |
| database (static) | OfficerApplicationDatabase | Database of all officer applications |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| OfficerApplication() | | | Default constructor |
| OfficerApplication() | LocalDate applicationDate, Officer officer, Project project, OfficerApplicationStatusEnum status | | Full constructor |
| setApplicationDate() | LocalDate applicationDate | void | Sets application date |
| setOfficer() | Officer officer | void | Sets officer |
| setProject() | Project project | void | Sets project |
| setStatus() | OfficerApplicationStatusEnum status | void | Sets status |
| setDatabase() (static) | OfficerApplicationDatabase database | void | Sets the officer application database |
| getApplicationDate() | | LocalDate | Gets application date |
| getOfficer() | | Officer | Gets officer |
| getProject() | | Project | Gets project |
| getStatus() | | OfficerApplicationStatusEnum | Gets status |
| getDatabase() (static) | | OfficerApplicationDatabase | Gets the officer application database |
| findApplicationsByDate() (static) | LocalDate date | ArrayList<OfficerApplication> | Finds applications by date |
| findApplicationsByOfficer() (static) | Officer officer | ArrayList<OfficerApplication> | Finds applications by officer |
| findApplicationsByProject() (static) | Project project | ArrayList<OfficerApplication> | Finds applications by project |
| findApplicationsByStatus() (static) | OfficerApplicationStatusEnum status | ArrayList<OfficerApplication> | Finds applications by status |
| addToDatabase() (static) | OfficerApplication application | boolean | Adds application to database |
| removeFromDatabase() (static) | OfficerApplication application | boolean | Removes application from database |
| getAllApplications() (static) | | ArrayList<OfficerApplication> | Gets all applications in database |
| toString() | | String | Returns string representation of application |

### Project
| Attribute | Type | Description |
|-----------|------|-------------|
| projectName | String | Name of project |
| neighborhood | String | Neighborhood of project |
| applicationStartDate | LocalDate | Start date for applications |
| applicationEndDate | LocalDate | End date for applications |
| manager | Manager | Manager of project |
| officerSlots | int | Number of officer slots |
| visibility | VisibilityEnum | Visibility of project |
| flatTypes | ArrayList<FlatType> | Flat types in project |
| assignedOfficers | ArrayList<Officer> | Officers assigned to project |
| database (static) | ProjectDatabase | Database of all projects |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| Project() | | | Default constructor |
| Project() | String projectName, String neighborhood, LocalDate applicationStartDate, LocalDate applicationEndDate, ArrayList<FlatType> flatTypes, Manager manager, int officerSlots, ArrayList<Officer> assignedOfficers, VisibilityEnum visibility | | Full constructor |
| setProjectName() | String projectName | void | Sets project name |
| setNeighborhood() | String neighborhood | void | Sets neighborhood |
| setApplicationStartDate() | LocalDate applicationStartDate | void | Sets application start date |
| setApplicationEndDate() | LocalDate applicationEndDate | void | Sets application end date |
| setManager() | Manager manager | void | Sets manager |
| setOfficerSlots() | int officerSlots | void | Sets number of officer slots |
| setVisibility() | VisibilityEnum visibility | void | Sets visibility |
| setFlatTypes() | ArrayList<FlatType> flatTypes | void | Sets flat types |
| setAssignedOfficers() | ArrayList<Officer> assignedOfficers | void | Sets assigned officers |
| setDatabase() (static) | ProjectDatabase database | void | Sets the project database |
| getProjectName() | | String | Gets project name |
| getNeighborhood() | | String | Gets neighborhood |
| getApplicationStartDate() | | LocalDate | Gets application start date |
| getApplicationEndDate() | | LocalDate | Gets application end date |
| getManager() | | Manager | Gets manager |
| getOfficerSlots() | | int | Gets number of officer slots |
| getVisibility() | | VisibilityEnum | Gets visibility |
| getFlatTypes() | | ArrayList<FlatType> | Gets flat types |
| getAssignedOfficers() | | ArrayList<Officer> | Gets assigned officers |
| getDatabase() (static) | | ProjectDatabase | Gets the project database |
| addFlatType() | FlatType flatType | void | Adds flat type to project |
| assignOfficer() | Officer officer | boolean | Assigns officer to project |
| unassignOfficer() | Officer officer | boolean | Unassigns officer from project |
| isOfficerAssigned() | Officer officer | boolean | Checks if officer is assigned to project |
| isApplicationOpen() | | boolean | Checks if application period is currently open |
| findProjectByName() (static) | String projectName | Project | Finds project by name |
| findProjectsByNeighborhood() (static) | String neighborhood | ArrayList<Project> | Finds projects by neighborhood |
| findProjectsByApplicationStartDate() (static) | LocalDate startDate | ArrayList<Project> | Finds projects by application start date |
| findProjectsByApplicationEndDate() (static) | LocalDate endDate | ArrayList<Project> | Finds projects by application end date |
| findProjectsByFlatType() (static) | FlatTypeEnum flatType | ArrayList<Project> | Finds projects by flat type |
| findProjectsByManager() (static) | Manager manager | ArrayList<Project> | Finds projects by manager |
| findProjectsByOfficer() (static) | Officer officer | ArrayList<Project> | Finds projects by officer |
| findProjectsByVisibility() (static) | VisibilityEnum visibility | ArrayList<Project> | Finds projects by visibility |
| addToDatabase() (static) | Project project | boolean | Adds project to database |
| removeFromDatabase() (static) | Project project | boolean | Removes project from database |
| getAllProjects() (static) | | ArrayList<Project> | Gets all projects in database |
| toString() | | String | Returns string representation of project |

### Receipt
| Attribute | Type | Description |
|-----------|------|-------------|
| receiptNumber | String | Unique receipt number |
| date | LocalDate | Date of receipt |
| booking | Booking | Associated booking |
| database (static) | ReceiptDatabase | Database of all receipts |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| Receipt() | | | Default constructor |
| Receipt() | String receiptNumber, LocalDate date, Booking booking | | Full constructor |
| setReceiptNumber() | String receiptNumber | void | Sets receipt number |
| setDate() | LocalDate date | void | Sets date |
| setBooking() | Booking booking | void | Sets booking |
| setDatabase() (static) | ReceiptDatabase database | void | Sets the receipt database |
| getReceiptNumber() | | String | Gets receipt number |
| getDate() | | LocalDate | Gets date |
| getBooking() | | Booking | Gets booking |
| getDatabase() (static) | | ReceiptDatabase | Gets the receipt database |
| generateReceiptNumber() (private) | | String | Generates a unique receipt number |
| findReceiptByNumber() (static) | String receiptNumber | Receipt | Finds receipt by number |
| findReceiptByDate() (static) | LocalDate date | ArrayList<Receipt> | Finds receipts by date |
| findReceiptByBooking() (static) | Booking booking | Receipt | Finds receipt by booking |
| addToDatabase() (static) | Receipt receipt | boolean | Adds receipt to database |
| removeFromDatabase() (static) | Receipt receipt | boolean | Removes receipt from database |
| getAllReceipts() (static) | | ArrayList<Receipt> | Gets all receipts in database |
| toString() | | String | Returns string representation of receipt |

## Database Classes

### ApplicantDatabase
| Attribute | Type | Description |
|-----------|------|-------------|
| applicants | ArrayList<Applicant> | List of applicants |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| ApplicantDatabase() | | | Default constructor |
| setApplicants() | ArrayList<Applicant> applicants | void | Sets list of applicants |
| getApplicants() | | ArrayList<Applicant> | Gets list of applicants |
| printData() | | void | Prints database data |

### BookingDatabase
| Attribute | Type | Description |
|-----------|------|-------------|
| bookings | ArrayList<Booking> | List of bookings |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| BookingDatabase() | | | Default constructor |
| setBookings() | ArrayList<Booking> bookings | void | Sets list of bookings |
| getBookings() | | ArrayList<Booking> | Gets list of bookings |
| printData() | | void | Prints database data |

### BTOApplicationDatabase
| Attribute | Type | Description |
|-----------|------|-------------|
| applications | ArrayList<BTOApplication> | List of BTO applications |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| BTOApplicationDatabase() | | | Default constructor |
| setApplications() | ArrayList<BTOApplication> applications | void | Sets list of applications |
| getApplications() | | ArrayList<BTOApplication> | Gets list of applications |
| printData() | | void | Prints database data |

### EnquiryDatabase
| Attribute | Type | Description |
|-----------|------|-------------|
| enquiries | ArrayList<Enquiry> | List of enquiries |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| EnquiryDatabase() | | | Default constructor |
| setEnquiries() | ArrayList<Enquiry> enquiries | void | Sets list of enquiries |
| getEnquiries() | | ArrayList<Enquiry> | Gets list of enquiries |
| printData() | | void | Prints database data |

### ManagerDatabase
| Attribute | Type | Description |
|-----------|------|-------------|
| managers | ArrayList<Manager> | List of managers |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| ManagerDatabase() | | | Default constructor |
| setManagers() | ArrayList<Manager> managers | void | Sets list of managers |
| getManagers() | | ArrayList<Manager> | Gets list of managers |
| printData() | | void | Prints database data |

### OfficerApplicationDatabase
| Attribute | Type | Description |
|-----------|------|-------------|
| applications | ArrayList<OfficerApplication> | List of officer applications |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| OfficerApplicationDatabase() | | | Default constructor |
| setApplications() | ArrayList<OfficerApplication> applications | void | Sets list of applications |
| getApplications() | | ArrayList<OfficerApplication> | Gets list of applications |
| printData() | | void | Prints database data |

### OfficerDatabase
| Attribute | Type | Description |
|-----------|------|-------------|
| officers | ArrayList<Officer> | List of officers |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| OfficerDatabase() | | | Default constructor |
| setOfficers() | ArrayList<Officer> officers | void | Sets list of officers |
| getOfficers() | | ArrayList<Officer> | Gets list of officers |
| printData() | | void | Prints database data |

### ProjectDatabase
| Attribute | Type | Description |
|-----------|------|-------------|
| projects | ArrayList<Project> | List of projects |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| ProjectDatabase() | | | Default constructor |
| setProjects() | ArrayList<Project> projects | void | Sets list of projects |
| getProjects() | | ArrayList<Project> | Gets list of projects |
| printData() | | void | Prints database data |

### ReceiptDatabase
| Attribute | Type | Description |
|-----------|------|-------------|
| receipts | ArrayList<Receipt> | List of receipts |

| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| ReceiptDatabase() | | | Default constructor |
| setReceipts() | ArrayList<Receipt> receipts | void | Sets list of receipts |
| getReceipts() | | ArrayList<Receipt> | Gets list of receipts |
| printData() | | void | Prints database data |

## Enum Classes

### BTOApplicationStatusEnum
| Values |
|--------|
| PENDING |
| SUCCESSFUL |
| REJECTED |

### BookingStatusEnum
| Values |
|--------|
| PENDING |
| CONFIRMED |
| CANCELLED |

### EnquiryStatusEnum
| Values |
|--------|
| PENDING |
| REPLIED |

### FlatTypeEnum
| Values |
|--------|
| TWO_ROOM |
| THREE_ROOM |

### MarriageStatusEnum
| Values |
|--------|
| SINGLE |
| MARRIED |

### OfficerApplicationStatusEnum
| Values |
|--------|
| PENDING |
| APPROVED |
| REJECTED |

### VisibilityEnum
| Values |
|--------|
| VISIBLE |
| HIDDEN |

### WithdrawalStatusEnum
| Values |
|--------|
| NA |
| PENDING |
| APPROVED |
| REJECTED |

## Interface Classes

### IDisplayable
| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| display() | | void | Displays Menu |

### IDatabasePrintable
| Method | Parameters | Return Type | Description |
|--------|------------|-------------|-------------|
| printData() | | void | Prints data from database |
