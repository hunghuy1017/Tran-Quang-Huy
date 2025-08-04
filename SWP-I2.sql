
	-- Create the database
	CREATE DATABASE SwimmingPoolManagement3;
	GO

	-- Use the newly created database
	USE SwimmingPoolManagement3;
	GO

	---
	--- 1. Roles (based on SwimmingPoolManagement5)
	CREATE TABLE Roles (
		RoleID INT PRIMARY KEY IDENTITY(1,1),
		RoleName NVARCHAR(100),
		Status BIT DEFAULT 1,
		Description NVARCHAR(255)
	);
	go
	-- 2. SwimmingPools (based on SwimmingPoolManagement5)
	CREATE TABLE SwimmingPools (
		PoolID INT PRIMARY KEY IDENTITY(1,1),
		Name NVARCHAR(100),
		Location NVARCHAR(200),
		Phone NVARCHAR(20),
		Fanpage NVARCHAR(100),
		OpenTime TIME,
		CloseTime TIME,
		Description NVARCHAR(255),
		Status BIT,
		Image NVARCHAR(255),
	);
	go
	-- 3. Users (based on SwimmingPoolManagement5)
	CREATE TABLE Users (
		UserID INT PRIMARY KEY IDENTITY(1,1),
		RoleID INT FOREIGN KEY REFERENCES Roles(RoleID),
		FullName NVARCHAR(100),
		Email NVARCHAR(100),
		Password NVARCHAR(100),
		Phone NVARCHAR(20),
		Address NVARCHAR(200),
		Image NVARCHAR(255),
		CreatedAt DATETIME DEFAULT GETDATE() -- Đã thêm cột này
	);
	go

	CREATE TABLE Categories (
		CategoryID INT PRIMARY KEY IDENTITY(1,1),
		CategoryName NVARCHAR(100) NOT NULL,
		Description NVARCHAR(255)
	);
	go

	-- 4. Product (based on SwimmingPoolManagement5)
	CREATE TABLE Product (
		ProductID INT PRIMARY KEY IDENTITY(1,1),
		PoolID INT FOREIGN KEY REFERENCES SwimmingPools(PoolID),
		ProductName NVARCHAR(100) NOT NULL,
		Description NVARCHAR(500),
		Price DECIMAL(10,2) DEFAULT 0,
		AddedDate DATETIME DEFAULT GETDATE(),
		Quantity INT NOT NULL DEFAULT 0,
		Image NVARCHAR(255),
		Status INT DEFAULT 1,
		CategoryID INT FOREIGN KEY REFERENCES Categories(CategoryID),
		IsRentable BIT DEFAULT 0,
		RentalPrice DECIMAL(10,2) DEFAULT 0
	);
	GO

	-- 5. Package (based on SwimmingPoolManagement5)
	CREATE TABLE Package (
		PackageID INT PRIMARY KEY IDENTITY(1,1),
		PackageName NVARCHAR(50) NOT NULL,
		DurationInDays INT NOT NULL,
		Price DECIMAL(10, 2) NOT NULL,
		Description NVARCHAR(255) NULL,
		IsActive BIT DEFAULT 1,
		CreatedAt DATETIME DEFAULT GETDATE()
	);
	go
	CREATE TABLE Payments (
		PaymentID INT PRIMARY KEY IDENTITY(1,1),
		UserID INT NOT NULL,
		PackageID INT NOT NULL,
		PoolID INT NOT NULL,
		PaymentMethod NVARCHAR(50) NOT NULL,      -- 'VNPay', 'PayPal'
		Total DECIMAL(10,2) NOT NULL,            -- Giá gói
		Status NVARCHAR(50) DEFAULT N'Pending',   -- 'Pending', 'Completed', 'Failed'
		PaymentTime DATETIME DEFAULT GETDATE(),

		CONSTRAINT FK_Payments_Users FOREIGN KEY (UserID) REFERENCES Users(UserID),
		CONSTRAINT FK_Payments_Package FOREIGN KEY (PackageID) REFERENCES Package(PackageID),
		CONSTRAINT FK_Payments_Pools FOREIGN KEY (PoolID) REFERENCES SwimmingPools(PoolID)
	);
	go

	-- 6. Blogs (based on SwimmingPoolManagement5)
	CREATE TABLE Blogs (
		BlogID INT PRIMARY KEY IDENTITY(1,1),
		AuthorID INT FOREIGN KEY REFERENCES Users(UserID),
		Title NVARCHAR(255),
		Content NVARCHAR(MAX),
		CreatedAt DATETIME
	);
	go
	-- 7. Events (based on SwimmingPoolManagement5)
	CREATE TABLE Events (
		EventID INT PRIMARY KEY IDENTITY(1,1),
		PoolID INT FOREIGN KEY REFERENCES SwimmingPools(PoolID),
		CreatedBy INT FOREIGN KEY REFERENCES Users(UserID),
		EventDate DATE,
		Title NVARCHAR(255),
		Description NVARCHAR(255),
		Image NVARCHAR(255),
		EndDate DATE,
		DetailedDescription NVARCHAR(MAX)
	);
	go
	-- 8. UserSchedules (based on SwimmingPoolManagement5)
	CREATE TABLE UserSchedules (
		ScheduleID INT PRIMARY KEY IDENTITY(1,1),
		UserID INT FOREIGN KEY REFERENCES Users(UserID),
		StartTime DATETIME,
		EndTime DATETIME,
		Task NVARCHAR(255)
	);
	go
	-- 9. SwimHistory (based on SwimmingPoolManagement5)
	CREATE TABLE SwimHistory (
		HistoryID INT PRIMARY KEY IDENTITY(1,1),
		UserID INT FOREIGN KEY REFERENCES Users(UserID),
		PoolID INT FOREIGN KEY REFERENCES SwimmingPools(PoolID),
		SwimDate DATE
	);
	go
	-- 10. [Order] (based on SwimmingPoolManagement5)
	CREATE TABLE [Order] (
		OrderID INT PRIMARY KEY IDENTITY(1,1),
		UserID INT FOREIGN KEY REFERENCES Users(UserID),
		Total INT,
		OrderDate DATETIME,
		Status bit
	);
	go
	CREATE TABLE OrderDetails (
		detailID INT IDENTITY(1,1) PRIMARY KEY,
		price DECIMAL(10, 2),
		quantity INT,
		Status bit,
		OrderID INT FOREIGN KEY REFERENCES [Order](OrderID),
		ProductID INT FOREIGN KEY REFERENCES Product(ProductID)
	)
	Go
	CREATE TABLE Classes (
		ClassID          INT IDENTITY(1,1) PRIMARY KEY,
		TrainerID        INT NOT NULL FOREIGN KEY REFERENCES Users(UserID),
		PoolID           INT NOT NULL FOREIGN KEY REFERENCES SwimmingPools(PoolID),
		ClassName        NVARCHAR(100) NOT NULL,
		ClassDate        DATE NOT NULL,
		StartTime        TIME NOT NULL,
		EndTime          TIME NOT NULL,
		MaxParticipants  INT NOT NULL,
		Description      NVARCHAR(255)
	);
	go
	/* =============================
	   2)  BẢNG TRAINERBOOKINGS
	   ============================= */
	CREATE TABLE TrainerBookings (
		BookingID   INT IDENTITY(1,1) PRIMARY KEY,
		UserID      INT NOT NULL FOREIGN KEY REFERENCES Users(UserID),
		PoolID      INT NOT NULL FOREIGN KEY REFERENCES SwimmingPools(PoolID),
		TrainerID   INT NOT NULL FOREIGN KEY REFERENCES Users(UserID),
		ClassID     INT NULL       FOREIGN KEY REFERENCES Classes(ClassID),
		BookingDate DATE NOT NULL,
		StartTime   TIME NOT NULL,
		EndTime     TIME NOT NULL,
		BookPrice   DECIMAL(10,2) NOT NULL,
		Status      NVARCHAR(50)  DEFAULT N'Pending',
		UserName    NVARCHAR(100),
		Note        NVARCHAR(255)
	);
	go
	-- 11. TrainerBookings (based on SwimmingPoolManagement5)
	CREATE TABLE TrainerBookingPayments (
		PaymentID     INT IDENTITY(1,1) PRIMARY KEY,
		BookingID     INT NOT NULL UNIQUE
					 FOREIGN KEY REFERENCES TrainerBookings(BookingID),
		PaymentDate   DATETIME     DEFAULT GETDATE(),
		Amount        DECIMAL(10,2) NOT NULL,
		PaymentMethod NVARCHAR(50) NOT NULL,
		TransactionID NVARCHAR(100) NOT NULL,
		PaymentStatus NVARCHAR(50) DEFAULT N'Completed'
	);
	go
	-- 12. UserReviews (based on SwimmingPoolManagement5)
	CREATE TABLE UserReviews (
		ReviewID INT PRIMARY KEY IDENTITY(1,1),
		UserID INT FOREIGN KEY REFERENCES Users(UserID),
		PoolID INT FOREIGN KEY REFERENCES SwimmingPools(PoolID),
		Rating INT,
		Comment NVARCHAR(255),
		CreatedAt DATETIME
	);
	go 
	-- 13. Employee - CHỈ THÊM HourlyRate
	CREATE TABLE Employee (
		UserID INT PRIMARY KEY,
		Position NVARCHAR(255),
		Description NVARCHAR(255),
		StartDate DATE,
		EndDate DATE,
		Attendance INT,
		Salary DECIMAL(10,2),
		PoolID INT,
		HourlyRate DECIMAL(10,2), -- THÊM CỘT
		CONSTRAINT FK_Employee_User FOREIGN KEY (UserID) REFERENCES Users(UserID),
		CONSTRAINT FK_Employee_Pool FOREIGN KEY (PoolID) REFERENCES SwimmingPools(PoolID)
	);
	GO

	-- 15. Notifications (based on SwimmingPoolManagement5)
	CREATE TABLE Notifications (
		NotificationID INT PRIMARY KEY IDENTITY(1,1),
		UserID INT FOREIGN KEY REFERENCES Users(UserID),
		Title NVARCHAR(255),
		Message NVARCHAR(255),
		IsRead BIT,
		CreatedAt DATETIME
	);
	go
	-- 16. UserPackages (based on SwimmingPoolManagement5)
	CREATE TABLE UserPackages (
		UserPackageID INT PRIMARY KEY IDENTITY(1,1),
		UserID INT FOREIGN KEY REFERENCES Users(UserID),
		PoolID INT FOREIGN KEY REFERENCES SwimmingPools(PoolID),
		PackageID INT FOREIGN KEY REFERENCES Package(PackageID),
		PurchaseDate DATETIME DEFAULT GETDATE(),
		StartDate DATE NOT NULL,
		EndDate DATE NOT NULL,
		IsActive BIT,
		PaymentStatus NVARCHAR(50) DEFAULT N'Pending'
	);
	go
	-- 17. TrainerReviews (based on SwimmingPoolManagement5)
	CREATE TABLE TrainerReviews (
		ReviewID INT PRIMARY KEY IDENTITY(1,1),
		UserID INT FOREIGN KEY REFERENCES Users(UserID),
		TrainerID INT FOREIGN KEY REFERENCES Users(UserID),
		Rating INT,
		Comment NVARCHAR(255),
		CreatedAt DATETIME
	);
	go
	-- 18. PoolPackages (based on SwimmingPoolManagement5)
	CREATE TABLE PoolPackages (
		PoolPackageID INT PRIMARY KEY IDENTITY(1,1),
		PoolID INT FOREIGN KEY REFERENCES SwimmingPools(PoolID),
		PackageID INT FOREIGN KEY REFERENCES Package(PackageID),
		EventID INT FOREIGN KEY REFERENCES Events(EventID) NULL,
		AvailabilityStatus BIT DEFAULT 1,
		Notes NVARCHAR(255)
	);
	go
	-- 19. EventRegistrations (based on SwimmingPoolManagement5)
	CREATE TABLE EventRegistrations (
		RegistrationID INT PRIMARY KEY IDENTITY(1,1),
		EventID INT FOREIGN KEY REFERENCES Events(EventID),
		UserID INT FOREIGN KEY REFERENCES Users(UserID),
		RegisteredAt DATETIME DEFAULT GETDATE(),
		Status NVARCHAR(50) DEFAULT N'Pending',
		Note NVARCHAR(255),
		FullName NVARCHAR(100) NOT NULL DEFAULT N'',
		Phone NVARCHAR(20) NOT NULL DEFAULT N'',
		Email NVARCHAR(100) NOT NULL DEFAULT N'',
		Address NVARCHAR(255) NOT NULL DEFAULT N''
	);
	go

	-- Insert data into Roles
	INSERT INTO Roles (RoleName, Status, Description) VALUES
	('admin', 1, N'Manages the entire system and oversees operations'),
	('user', 1, N'Regular users who access pool services'),
	('employee', 1, N'Staff handling pool operations and maintenance'),
	('trainer', 1, N'Professional swim instructors');

	-- Insert data into SwimmingPools
	INSERT INTO SwimmingPools (Name, Location, Phone, Fanpage, OpenTime, CloseTime, Description, Status, Image) VALUES
	('Sunset Pool', '123 Main St, Hanoi', '0123456789', 'facebook.com/sunsetpool', '06:00:00', '21:00:00', 'Olympic-sized pool with modern facilities', 1, 'sunset.jpg'),
	('Blue Wave Pool', '456 Tran Phu, Ho Chi Minh City', '0234567890', 'facebook.com/bluewavepool', '08:00:00', '20:00:00', 'Family-friendly pool with slides', 1, 'bluewave.jpg'),
	('Starlight Pool', '789 Nguyen Trai, Da Nang', '0345678901', 'facebook.com/starlightpool', '07:00:00', '22:00:00', 'Indoor pool with heating system', 0, 'starlight.jpg'),
	('Moonlight Pool', '101 Le Van Sy, Ho Chi Minh City', '0456789012', 'facebook.com/moonlightpool', '09:00:00', '23:00:00', 'Outdoor pool with night lighting', 1, 'moonlight.jpg'),
	('Crystal Lake Pool', '222 Ba Dinh, Hanoi', '0567890123', 'facebook.com/crystallakepool', '06:30:00', '20:30:00', 'Large pool for competitive swimming', 1, 'crystallake.jpg'),
	('Ocean Breeze Pool', '333 Hai Phong, Da Nang', '0678901234', 'facebook.com/oceanbreezepool', '08:00:00', '19:00:00', 'Pool with ocean view', 0, 'oceanbreeze.jpg'),
	('Golden Sun Pool', '555 Hoang Hoa Tham, Hanoi', '0789012345', 'facebook.com/goldensunpool', '07:00:00', '22:00:00', 'Luxury pool with spa facilities', 1, 'goldensun.jpg'),
	('Aqua Park Pool', '666 Nguyen Van Cu, Ho Chi Minh City', '0890123456', 'facebook.com/aquaparkpool', '09:00:00', '21:00:00', 'Pool with water park features', 1, 'aquapark.jpg'),
	('Silver Wave Pool', '777 Ly Thuong Kiet, Da Nang', '0901234567', 'facebook.com/silverwavepool', '06:00:00', '20:00:00', 'Training pool for athletes', 1, 'silverwave.jpg'),
	('Emerald Pool', '888 Le Lai, Ho Chi Minh City', '0912345678', 'facebook.com/emeraldpool', '07:30:00', '21:30:00', 'Eco-friendly pool with solar heating', 1, 'emerald.jpg'),
	('Skyline Pool', '999 Hoang Dieu, Hanoi', '0923456789', 'facebook.com/skylinepool', '08:00:00', '22:00:00', 'Rooftop pool with city views', 1, 'skyline.jpg'),
	('Coral Reef Pool', '111 Ba Huyen Thanh Quan, Da Nang', '0934567890', 'facebook.com/coralreefpool', '09:00:00', '20:00:00', 'Themed pool for relaxation', 0, 'coralreef.jpg'),
	('Pearl Pool', '222 Nguyen Thi Minh Khai, Ho Chi Minh City', '0945678901', 'facebook.com/pearlpool', '07:00:00', '22:00:00', 'Luxury indoor pool with jacuzzi', 1, 'pearl.jpg'),
	('Riverfront Pool', '333 Hang Bong, Hanoi', '0956789012', 'facebook.com/riverfrontpool', '06:30:00', '21:00:00', 'Outdoor pool with scenic views', 1, 'riverfront.jpg'),
	('Tropical Oasis Pool', '444 Tran Hung Dao, Da Nang', '0967890123', 'facebook.com/tropicaloasispool', '08:00:00', '20:00:00', 'Tropical-themed pool for families', 0, 'tropicaloasis.jpg');

	INSERT INTO Users (RoleID, FullName, Email, Password, Phone, Address, Image, CreatedAt) VALUES
	(1, N'Nguyễn Văn An', 'an.nguyen@example.com', 'pass123', '0901234567', '12 Hoan Kiem, Hanoi', 'an_profile.jpg', '2024-01-15 10:00:00'),
	(2, N'Trần Thị Ánh', 'loxum121204@gmail.com', 'pass456', '0912345678', N'Hải Dương', N'avatar_user_2_1748273096305_3460393.webp', '2024-02-20 14:30:00'),
	(3, N'Phạm Minh Cường', 'cuong.pham@example.com', 'pass789', '0923456789', '20 Hai Ba Trung, Hanoi', N'avatar_user_3_1748437717415_dark.png', '2024-03-01 09:15:00'),
	(4, N'Lê Thị Duyên', 'duyen.le@example.com', 'pass101', '0934567890', '30 Nguyen Hue, HCMC', 'duyen_profile.jpg', '2024-04-10 11:00:00'),
	(2, N'Hoàng Văn Em', N'Bạn chưa thêm Email', 'pass202', '0945678901', '45 Le Loi, Da Nang', N'avatar_user_5_1748308573915_3460393.webp', '2024-05-05 16:00:00'),
	(2, N'Vũ Thị Hương', 'huong.vu@example.com', 'pass303', '0956789012', '50 Ba Dinh, Hanoi', 'huong_profile.jpg', '2024-05-12 10:30:00'),
	(2, N'Đỗ Văn Khang', 'khang.do@example.com', 'pass404', '0967890123', '60 District 3, HCMC', 'khang_profile.jpg', '2024-05-18 09:00:00'),
	(3, N'Bùi Minh Long', 'long.bui@example.com', 'pass505', '0978901234', '70 Ngo Quyen, Da Nang', N'avatar_user_8_1748276705832_63882505702905.png', '2024-06-01 13:00:00'),
	(4, N'Ngô Thị Mai', 'mai.ngo@example.com', 'pass606', '0989012345', N'Hanoi', N'avatar_user_9_1748274036034_a503184c6272c3ecc1dbaeb6888d9833.png', '2024-06-05 10:45:00'),
	(2, N'Phan Văn Đức', 'duc.phan@example.com', 'pass707', '0990123456', '90 Dong Da, Hanoi', 'duc_profile.jpg', '2024-06-10 11:30:00'),
	(2, N'Nguyễn Thị Hằng', 'hang.nguyen@example.com', 'pass808', '0991234567', '100 District 7, HCMC', 'hang_profile.jpg', '2024-06-15 14:00:00'),
	(3, N'Trương Minh Khoa', 'khoa.truong@example.com', 'pass909', '0992345678', '110 Quang Trung, Da Nang', 'khoa_profile.jpg', '2024-06-20 09:00:00'),
	(4, N'Võ Thị Lan', 'lan.vo@example.com', 'pass1010', '0993456789', '120 Nguyen Trai, Hanoi', 'lan_profile.jpg', '2024-06-21 10:00:00'),
	(2, N'Đinh Văn Nam', 'nam.dinh@example.com', 'pass1111', '0994567890', '130 Ly Thai To, Hanoi', 'nam_profile.jpg', '2024-06-22 11:00:00'),
	(2, N'Phùng Thị Oanh', 'oanh.phung@example.com', 'pass1212', '0995678901', '140 District 5, HCMC', 'oanh_profile.jpg', '2024-06-22 15:00:00'),
	(3, N'Lương Minh Phong', 'phong.luong@example.com', 'pass1313', '0996789012', '150 Tran Phu, Da Nang', 'phong_profile.jpg', '2024-06-23 09:00:00'),
	(4, N'Hà Thị Quyên', 'quyen.ha@example.com', 'pass1414', '0997890123', '160 Nguyen Van Troi, Hanoi', 'quyen_profile.jpg', '2024-06-23 10:00:00'),
	(2, N'Nguyễn Văn Bảo', 'bao.nguyen@example.com', 'pass1515', '0998901234', '170 Le Duan, Hanoi', 'bao_profile.jpg', '2024-06-23 11:00:00'),
	(3, N'Trần Thị Cẩm', 'cam.tran@example.com', 'pass1616', '0999012345', '180 District 2, HCMC', 'cam_profile.jpg', '2024-06-23 12:00:00'),
	(4, N'Phạm Văn Đạt', 'dat.pham@example.com', 'pass1717', '0999123456', '190 Hai Ba Trung, Da Nang', 'dat_profile.jpg', '2024-06-23 13:00:00'),
	(2, N'Lê Thị Hồng', 'hong.le@example.com', 'pass1818', '0999234567', '200 Nguyen Hue, Hanoi', 'hong_profile.jpg', '2024-06-23 14:00:00'),
	(3, N'Nguyễn Văn An', 'an.nguyen@example.com', '123456', '0901234567', N'12 Trần Hưng Đạo, Q1, TP.HCM', 'images/avatar1.png', '2024-01-01 08:00:00'),
	(3, N'Trần Thị Bình', 'binh.tran@example.com', '123456', '0902345678', N'45 Lê Lợi, Q3, TP.HCM', 'images/avatar2.png', '2024-01-05 09:00:00'),
	(3, N'Lê Văn Cường', 'cuong.le@example.com', '123456', '0903456789', N'78 Nguyễn Thị Minh Khai, Q10, TP.HCM', 'images/avatar3.png', '2024-01-10 10:00:00'),
	(3, N'Phạm Thị Dung', 'dung.pham@example.com', '123456', '0904567890', N'23 Võ Thị Sáu, Q1, TP.HCM', 'images/avatar4.png', '2024-01-15 11:00:00'),
	(3, N'Huỳnh Văn Đức', 'duc.huynh@example.com', '123456', '0905678901', N'90 Cách Mạng Tháng Tám, Q3, TP.HCM', 'images/avatar5.png', '2024-01-20 12:00:00'),
	(3, N'Đặng Thị Hoa', 'hoa.dang@example.com', '123456', '0906789012', N'56 Trường Chinh, Tân Bình, TP.HCM', 'images/avatar6.png', '2024-01-25 13:00:00'),
	(3, N'Võ Văn Huy', 'huy.vo@example.com', '123456', '0907890123', N'102 Tô Hiến Thành, Q10, TP.HCM', 'images/avatar7.png', '2024-01-30 14:00:00'),
	(3, N'Ngô Thị Hạnh', 'hanh.ngo@example.com', '123456', '0908901234', N'15 Phạm Văn Đồng, Thủ Đức, TP.HCM', 'images/avatar8.png', '2024-02-01 15:00:00'),
	(3, N'Bùi Văn Khoa', 'khoa.bui@example.com', '123456', '0909012345', N'39 Điện Biên Phủ, Bình Thạnh, TP.HCM', 'images/avatar9.png', '2024-02-05 16:00:00'),
	(3, N'Đỗ Thị Lan', 'lan.do@example.com', '123456', '0910123456', N'70 Nguyễn Văn Cừ, Q5, TP.HCM', 'images/avatar10.png', '2024-02-10 17:00:00');
	go

	INSERT INTO Categories (CategoryName, Description) VALUES
	(N'Đồ ăn', N'Sản phẩm thực phẩm bán tại hồ bơi'),
	(N'Phụ kiện bơi', N'Trang thiết bị và phụ kiện bơi lội');

	-- Insert data into Product
	INSERT INTO Product (PoolID, ProductName, Description, Price, AddedDate, Quantity, Image, Status, CategoryID) VALUES
	(1, N'Kính bơi', N'Kính bơi chống mờ', 120000, '2025-06-01', 100, 'goggles.jpg', 1, 2),
	(1, N'Mũ bơi', N'Mũ bơi silicone', 60000, '2025-06-02', 200, 'cap.jpg', 1, 2),
	(1, N'Chân vịt bơi', N'Chân vịt dùng để luyện tập', 220000, '2025-06-03', 50, 'fins.jpg', 1, 2),
	(1, N'Khăn bơi', N'Khăn sợi microfiber mau khô', 80000, '2025-06-04', 150, 'towel.jpg', 1, 2),
	(1, N'Túi bơi', N'Túi đựng đồ bơi chống nước', 150000, '2025-06-05', 80, 'bag.jpg', 1, 2),
	(1, N'Ván đập chân', N'Ván xốp hỗ trợ tập luyện bơi', 160000, '2025-06-06', 60, 'kickboard.jpg', 1, 2),
	(1, N'Nút tai bơi', N'Nút tai silicone chống nước', 40000, '2025-06-07', 200, 'earplugs.jpg', 1, 2),
	(1, N'Thanh năng lượng', N'Thanh ăn nhẹ giàu protein', 25000, '2025-06-16', 100, 'proteinbar.jpg', 1, 1),
	(1, N'Nước tăng lực', N'Bù nước và tăng năng lượng', 20000, '2025-06-16', 150, 'energydrink.jpg', 1, 1),
	(1, N'Ly trái cây', N'Trái cây tươi trộn sẵn', 25000, '2025-06-16', 80, 'fruitcup.jpg', 1, 1),
	(1, N'Nước đóng chai', N'Nước khoáng 500ml', 10000, '2025-06-16', 300, 'water.jpg', 1, 1),
	(1, N'Sinh tố', N'Sinh tố chuối và dâu tây', 30000, '2025-06-16', 60, 'smoothie.jpg', 1, 1),
	(1, N'Bánh mì kẹp', N'Bánh mì kẹp thịt gà và rau', 35000, '2025-06-16', 70, 'sandwich.jpg', 1, 1),
	(1, N'Sữa chua', N'Sữa chua Hy Lạp kèm mật ong', 15000, '2025-06-16', 90, 'yogurt.jpg', 1, 1),
	(1, N'Thanh granola', N'Thanh ngũ cốc và hạt dinh dưỡng', 20000, '2025-06-16', 110, 'granola.jpg', 1, 1),
	(1, N'Trà đá', N'Trà chanh pha lạnh', 15000, '2025-06-16', 130, 'icedtea.jpg', 1, 1),
	(1, N'Sữa socola', N'Sữa socola ít béo', 20000, '2025-06-16', 100, 'chocolatemilk.jpg', 1, 1);




	-- Insert data into Package
	INSERT INTO Package (PackageName, DurationInDays, Price, Description, IsActive, CreatedAt) VALUES
	(N'Weekly Pass - Standard', 7, 150000.00, N'Gói bơi tiêu chuẩn 7 ngày tại bể bơi bất kỳ.', 1, '2025-05-26 09:30:00'),
	(N'Monthly Unlimited - Basic', 30, 500000.00, N'Gói bơi không giới hạn hàng tháng, áp dụng cho các bể bơi cơ bản.', 1, '2025-05-26 09:30:00'),
	(N'Annual Gold Pass - Premium', 365, 4000000.00, N'Gói vàng hàng năm, bao gồm quyền truy cập tất cả các bể bơi cao cấp và tiện ích VIP.', 1, '2025-05-26 09:30:00'),
	(N'3-Month Training Pass', 90, 1200000.00, N'Gói 3 tháng dành cho việc luyện tập bơi, bao gồm 2 buổi hướng dẫn/tuần.', 1, '2025-05-26 09:30:00'),
	(N'Single Day Pass - Weekday', 1, 30000.00, N'Vé vào cửa một ngày (chỉ các ngày trong tuần).', 0, '2025-05-26 09:30:00'),
	(N'Weekend Family Pass', 2, 80000.00, N'Gói cuối tuần dành cho gia đình (tối đa 4 người), áp dụng cho các bể bơi thân thiện gia đình.', 1, '2025-05-27 10:00:00'),
	(N'Summer Swim Fest Special', 7, 200000.00, N'Gói đặc biệt cho sự kiện Summer Swim Fest, bao gồm quyền tham gia các hoạt động lễ hội.', 1, '2025-05-28 11:00:00'),
	(N'Kids Swim Day Pass', 1, 25000.00, N'Vé vào cửa một ngày dành cho trẻ em trong sự kiện Kids Swim Day.', 1, '2025-05-28 11:00:00'),
	(N'Night Swim Party Access', 1, 60000.00, N'Vé tham dự bữa tiệc bơi đêm tại các bể bơi được chọn.', 1, '2025-05-29 12:00:00'),
	(N'Luxury Pool Access - 5 Days', 5, 300000.00, N'Gói 5 ngày truy cập các bể bơi sang trọng có tiện ích như phòng xông hơi, jacuzzi.', 1, '2025-05-30 14:00:00');

	-- Thanh toán thành công qua VNPay
	INSERT INTO Payments (UserID, PackageID, PoolID, PaymentMethod, Total, Status, PaymentTime)
	VALUES (2, 2, 1, N'VNPay', 500000.00, N'Completed', '2025-06-20 10:00:00');

	-- Thanh toán đang chờ xử lý qua PayPal
	INSERT INTO Payments (UserID, PackageID, PoolID, PaymentMethod, Total, Status, PaymentTime)
	VALUES (5, 1, 2, N'PayPal', 150000.00, N'Pending', '2025-06-21 08:30:00');

	-- Thanh toán thất bại
	INSERT INTO Payments (UserID, PackageID, PoolID, PaymentMethod, Total, Status, PaymentTime)
	VALUES (7, 3, 7, N'VNPay', 4000000.00, N'Failed', '2025-06-19 14:15:00');

	-- Thanh toán thành công qua VNPay
	INSERT INTO Payments (UserID, PackageID, PoolID, PaymentMethod, Total, Status, PaymentTime)
	VALUES (10, 4, 9, N'VNPay', 1200000.00, N'Completed', '2025-06-18 09:45:00');

	-- Thanh toán chờ xử lý (có thể vừa nhấn xác nhận nhưng chưa redirect)
	INSERT INTO Payments (UserID, PackageID, PoolID, PaymentMethod, Total)
	VALUES (11, 1, 1, N'PayPal', 150000.00); 

	-- Insert data into Blogs
	INSERT INTO Blogs (AuthorID, Title, Content, CreatedAt) VALUES
	(1, 'Top 5 Swimming Tips', 'Learn the best techniques for freestyle swimming...', '2025-05-20 10:00:00'),
	(4, 'Why Swimming is Great for Health', 'Swimming improves cardiovascular health...', '2025-05-21 14:30:00'),
	(2, 'Choosing the Right Swim Gear', 'Guide to selecting swim goggles and caps...', '2025-05-22 09:15:00'),
	(2, 'Swimming for Beginners', 'Tips for new swimmers to build confidence...', '2025-05-23 11:00:00'),
	(9, 'Advanced Swim Drills', 'Improve your speed with these drills...', '2025-05-24 08:30:00'),
	(1, 'Pool Safety Guidelines', 'Essential safety tips for all swimmers...', '2025-05-24 10:00:00'),
	(2, 'Benefits of Early Morning Swimming', 'Start your day with a refreshing swim...', '2025-05-24 12:00:00'),
	(13, 'Swim Stroke Techniques', 'Master the butterfly stroke with these tips...', '2025-05-24 14:00:00'),
	(1, 'Maintaining Pool Hygiene', 'Best practices for clean pools...', '2025-05-24 16:00:00'),
	(2, 'Swimming and Mental Health', 'How swimming reduces stress and anxiety...', '2025-05-25 09:00:00'),
	(16, 'Pool Maintenance Tips', 'Keep your pool clean and safe...', '2025-05-25 11:00:00'),
	(17, 'Freestyle Stroke Guide', 'Master freestyle with these techniques...', '2025-05-25 13:00:00');

	-- Insert data into Events
	INSERT INTO Events (PoolID, CreatedBy, EventDate, EndDate, Title, Description, Image, DetailedDescription)
	VALUES 
	(1, 1, '2025-06-01', '2025-06-01', N'Summer Swim Fest', N'Annual swimming competition', 'event1.jpg', N'Chào đón mùa hè với cuộc thi bơi lội hấp dẫn tại Sunset Pool.'),
	(2, 4, '2025-06-15', '2025-06-15', N'Kids Swim Day', N'Fun day for kids with games', 'event2.jpg', N'Ngày hội bơi dành cho thiếu nhi với trò chơi và phần quà vui nhộn.'),
	(3, 3, '2025-07-01', '2025-07-01', N'Night Swim Party', N'Evening swim with music', 'event3.jpg', N'Bữa tiệc bơi đêm với ánh sáng lung linh và âm nhạc sôi động.'),
	(4, 2, '2025-06-10', '2025-06-10', N'Moonlight Swim Night', N'Evening swim event with lights', 'event4.jpg', N'Bơi đêm dưới ánh trăng tại Moonlight Pool, kết hợp với nhạc nhẹ thư giãn.'),
	(5, 1, '2025-06-20', '2025-06-20', N'Swim Championship', N'Regional swim competition', 'event5.jpg', N'Giải bơi khu vực với các nội dung tranh tài hấp dẫn và giải thưởng giá trị.'),
	(6, 9, '2025-07-05', '2025-07-05', N'Family Pool Day', N'Fun activities for families', 'event6.jpg', N'Ngày hội gia đình với hoạt động bơi lội và trò chơi dành cho mọi lứa tuổi.'),
	(7, 2, '2025-06-25', '2025-06-25', N'Summer Splash', N'Community swim event', 'event7.jpg', N'Sự kiện bơi cộng đồng kết nối và giao lưu mùa hè.'),
	(8, 1, '2025-07-01', '2025-07-01', N'Aqua Fun Day', N'Water games for all ages', 'event8.jpg', N'Thử thách các trò chơi nước dành cho mọi lứa tuổi tại Aqua Fun Day.'),
	(9, 13, '2025-07-10', '2025-07-10', N'Athlete Training Camp', N'Intensive swim training', 'event9.jpg', N'Trại huấn luyện bơi lội chuyên sâu dành cho vận động viên.'),
	(10, 2, '2025-07-15', '2025-07-15', N'Eco Swim Day', N'Promoting eco-friendly swimming', 'event10.jpg', N'Sự kiện bơi xanh, kết hợp tuyên truyền bảo vệ môi trường.'),
	(11, 1, '2025-07-20', '2025-07-20', N'Rooftop Swim Party', N'Evening event with skyline views', 'event11.jpg', N'Bữa tiệc bơi sang chảnh trên rooftop với tầm nhìn toàn thành phố.'),
	(12, 17, '2025-08-01', '2025-08-01', N'Relaxation Swim', N'Calm swim session with music', 'event12.jpg', N'Buổi bơi thư giãn với nhạc nhẹ và ánh sáng dịu tại Pearl Pool.'),
	(13, 1, '2025-08-05', '2025-08-05', N'Pearl Pool Opening', N'Grand opening of the new luxury pool', 'event13.jpg', N'Lễ khai trương Pearl Pool với quà tặng và khuyến mãi đặc biệt.'),
	(14, 2, '2025-08-10', '2025-08-10', N'River Swim Challenge', N'Competitive swim along the riverfront', 'event14.jpg', N'Thử thách bơi dọc bờ sông dành cho người yêu thể thao.'),
	(15, 19, '2025-08-15', '2025-08-15', N'Tropical Pool Party', N'Family-friendly tropical-themed event', 'event15.jpg', N'Tiệc bơi phong cách nhiệt đới với trò chơi và tiệc buffet.');


	-- Insert data into UserSchedules
	INSERT INTO UserSchedules (UserID, StartTime, EndTime, Task) VALUES
	(4, '2025-05-25 08:00:00', '2025-05-25 10:00:00', 'Swim Training Session'),
	(3, '2025-05-26 09:00:00', '2025-05-26 11:00:00', 'Lifeguard Duty'),
	(1, '2025-05-27 14:00:00', '2025-05-27 16:00:00', 'Admin Meeting'),
	(9, '2025-05-28 09:00:00', '2025-05-28 11:00:00', 'Swim Coaching Session'),
	(8, '2025-05-29 13:00:00', '2025-05-29 15:00:00', 'Pool Maintenance'),
	(2, '2025-05-30 10:00:00', '2025-05-30 12:00:00', 'User Activity Review'),
	(13, '2025-05-31 08:00:00', '2025-05-31 10:00:00', 'Swim Coaching'),
	(12, '2025-06-01 10:00:00', '2025-06-01 12:00:00', 'Lifeguard Shift'),
	(2, '2025-06-02 09:00:00', '2025-06-02 11:00:00', 'User Activity Review'),
	(17, '2025-06-03 08:00:00', '2025-06-03 10:00:00', 'Swim Coaching'),
	(16, '2025-06-04 09:00:00', '2025-06-04 11:00:00', 'Pool Maintenance'),
	(2, '2025-06-05 10:00:00', '2025-06-05 12:00:00', 'User Activity Review'),
	(19, '2025-06-06 08:00:00', '2025-06-06 10:00:00', 'Swim Coaching'),
	(20, '2025-06-07 09:00:00', '2025-06-07 11:00:00', 'Lifeguard Shift'),
	(2, '2025-06-08 10:00:00', '2025-06-08 12:00:00', 'User Activity Review');

	-- Insert data into SwimHistory
	INSERT INTO SwimHistory (UserID, PoolID, SwimDate) VALUES
	(2, 1, '2025-05-20'),
	(5, 2, '2025-05-21'),
	(4, 3, '2025-05-22'),
	(7, 4, '2025-05-23'),
	(8, 5, '2025-05-24'),
	(9, 6, '2025-05-24'),
	(11, 7, '2025-05-24'),
	(12, 8, '2025-05-24'),
	(13, 9, '2025-05-24'),
	(15, 10, '2025-05-25'),
	(16, 11, '2025-05-25'),
	(17, 12, '2025-05-25'),
	(18, 13, '2025-05-26'),
	(20, 14, '2025-05-26'),
	(19, 15, '2025-05-26');

	-- Insert data into [Order]
	INSERT INTO [Order] (UserID, Total, OrderDate, Status) VALUES 
	(1, 280000, '2025-06-05', 1), -- Kính bơi + Mũ bơi
	(2, 470000, '2025-06-07', 1), -- Chân vịt + Khăn bơi + Nút tai
	(3, 25000, '2025-06-10', 1),  -- Thanh năng lượng
	(4, 55000, '2025-06-12', 1),  -- Trà đá + Sữa chua
	(5, 160000, '2025-06-13', 1); -- Ván đập chân

	-- Đơn hàng 1: UserID = 1
	INSERT INTO OrderDetails (price, quantity, Status, OrderID, ProductID) VALUES
	(120000, 1, 1, 1, 1), -- Kính bơi
	(160000, 1, 1, 1, 6); -- Ván đập chân
	-- Đơn hàng 2: UserID = 2
	INSERT INTO OrderDetails (price, quantity, Status, OrderID, ProductID) VALUES
	(220000, 1, 1, 2, 3), -- Chân vịt
	(80000, 1, 1, 2, 4),  -- Khăn bơi
	(40000, 1, 1, 2, 7);  -- Nút tai
	-- Đơn hàng 3: UserID = 3
	INSERT INTO OrderDetails (price, quantity, Status, OrderID, ProductID) VALUES
	(25000, 1, 1, 3, 8); -- Thanh năng lượng
	-- Đơn hàng 4: UserID = 4
	INSERT INTO OrderDetails (price, quantity, Status, OrderID, ProductID) VALUES
	(15000, 1, 1, 4, 16), -- Trà đá
	(40000, 1, 1, 4, 14); -- Sữa chua
	-- Đơn hàng 5: UserID = 5
	INSERT INTO OrderDetails (price, quantity, Status, OrderID, ProductID) VALUES
	(160000, 1, 1, 5, 6); -- Ván đập chân

	INSERT INTO Classes
			(TrainerID, PoolID, ClassName, ClassDate,
			 StartTime, EndTime, MaxParticipants, Description)
	VALUES
	/* ---- Trainer 4 ---- */
	(4, 1,  N'Beginner Crawl Basics',      '2025-07-05', '08:00', '09:30', 10, N'Fundamentals of front-crawl for new swimmers'),
	(4, 1,  N'Backstroke Fundamentals',    '2025-07-12', '10:00', '11:30', 10, N'Entry-level backstroke drills and kicks'),
	(4, 2,  N'Adult Technique Refinement', '2025-07-19', '08:00', '09:30', 12, N'Stroke correction for adult learners'),
	(4, 2,  N'Turns & Push-Off Workshop',  '2025-07-26', '10:00', '11:30', 12, N'Flip-turns and efficient wall push-offs'),

	/* ---- Trainer 9 ---- */
	(9, 3,  N'Freestyle Endurance',        '2025-07-07', '15:00', '16:30', 15, N'Build stamina with long-set freestyle training'),
	(9, 3,  N'Dolphin Kick Clinic',        '2025-07-14', '15:00', '16:30', 12, N'Improve underwater dolphin kicks for speed'),
	(9, 4,  N'Race-Starts Mastery',        '2025-07-21', '17:00', '18:30', 12, N'Block starts, dives, and breakout drills'),

	/* ---- Trainer 13 ---- */
	(13, 5, N'Kids Splash & Learn',        '2025-07-09', '09:00', '10:00', 15, N'Play-based water confidence for ages 6-10'),
	(13, 5, N'Butterfly Made Easy',        '2025-07-16', '09:00', '10:00', 10, N'Progressive butterfly drills for intermediates'),
	(13, 6, N'IM Challenge Prep',          '2025-07-23', '11:00', '12:30', 12, N'Train for Individual Medley events'),

	/* ---- Trainer 17 ---- */
	(17, 4, N'Junior Competitive Squad',   '2025-07-10', '16:00', '17:30', 12, N'High-intensity sets for junior racers'),
	(17, 4, N'Tumble-Turn Intensive',      '2025-07-17', '16:00', '17:30', 10, N'Special focus on fast flip-turns'),
	(17, 7, N'Open-Water Simulation',      '2025-07-24', '07:00', '08:30', 15, N'Pack-swim tactics and sighting practice'),

	/* ---- Trainer 20 ---- */
	(20, 8, N'Lifeguard Skills Refresher', '2025-07-06', '13:00', '15:00', 12, N'Rescue techniques and first-aid drills'),
	(20, 8, N'Night Swim Conditioning',    '2025-07-13', '19:00', '20:30', 10, N'After-hours endurance and pacing session');

	/* 15 booking – mỗi lớp 1 học viên */
	INSERT INTO TrainerBookings(UserID, PoolID, TrainerID, ClassID, BookingDate, StartTime, EndTime,BookPrice, Status, UserName, Note)
	VALUES
	/* Classes của Trainer 4 (ClassID 1-4) */
	( 2 , 1,  4,  1, '2025-07-05', '08:00','09:30', 100000, N'Confirmed', N'Lê Văn An',       N'Crawl cơ bản'),
	( 5 , 1,  4,  2, '2025-07-12', '10:00','11:30', 100000, N'Confirmed', N'Hoàng Văn Em',    N'Tập backstroke'),
	( 6 , 2,  4,  3, '2025-07-19', '08:00','09:30', 110000, N'Pending',   N'Vũ Thị Hương',    N'Cải thiện kỹ thuật'),
	( 7 , 2,  4,  4, '2025-07-26', '10:00','11:30', 110000, N'Confirmed', N'Đỗ Văn Khang',    N'Turn & push-off'),

	/* Classes của Trainer 9 (ClassID 5-7) */
	(10 , 3,  9,  5, '2025-07-07', '15:00','16:30',  90000, N'Confirmed', N'Phan Văn Đức',    N'Freestyle endurance'),
	(11 , 3,  9,  6, '2025-07-14', '15:00','16:30',  95000, N'Pending',   N'Nguyễn Thị Hằng', N'Dolphin kick'),
	(14 , 4,  9,  7, '2025-07-21', '17:00','18:30', 115000, N'Confirmed', N'Đinh Văn Nam',    N'Starts & dives'),

	/* Classes của Trainer 13 (ClassID 8-10) */
	(15 , 5, 13,  8, '2025-07-09', '09:00','10:00',  85000, N'Confirmed', N'Phùng Thị Oanh',  N'Kids splash'),
	(18 , 5, 13,  9, '2025-07-16', '09:00','10:00',  95000, N'Pending',   N'Nguyễn Văn Bảo',  N'Butterfly drills'),
	(21 , 6, 13, 10, '2025-07-23', '11:00','12:30', 100000, N'Confirmed', N'Lê Thị Hồng',    N'IM practice'),

	/* Classes của Trainer 17 (ClassID 11-13) */
	(22 , 4, 17, 11, '2025-07-10', '16:00','17:30', 100000, N'Confirmed', N'Nguyễn Văn An',   N'Junior squad'),
	(23 , 4, 17, 12, '2025-07-17', '16:00','17:30',  95000, N'Confirmed', N'Trần Thị Bình',   N'Tumble-turn'),
	(24 , 7, 17, 13, '2025-07-24', '07:00','08:30', 110000, N'Pending',   N'Lê Văn Cường',   N'Open-water sim'),

	/* Classes của Trainer 20 (ClassID 14-15) */
	(25 , 8, 20, 14, '2025-07-06', '13:00','15:00', 120000, N'Confirmed', N'Phạm Thị Dung',   N'Lifeguard refresher'),
	(26 , 8, 20, 15, '2025-07-13', '19:00','20:30', 115000, N'Confirmed', N'Huỳnh Văn Đức',  N'Night conditioning');


	go 


	-- Insert data into TrainerBookings
	INSERT INTO TrainerBookingPayments(BookingID, PaymentDate, Amount,PaymentMethod, TransactionID, PaymentStatus)VALUES
	( 1,'2025-07-05 08:05',100000,N'Credit Card',   'TBPAY_001',N'Completed'),
	( 2,'2025-07-12 10:05',100000,N'Momo',          'TBPAY_002',N'Completed'),
	( 3,'2025-07-19 08:10',110000,N'Bank Transfer', 'TBPAY_003',N'Pending'),
	( 4,'2025-07-26 10:07',110000,N'Credit Card',   'TBPAY_004',N'Completed'),
	( 5,'2025-07-07 15:05', 90000,N'Cash',          'TBPAY_005',N'Completed'),
	( 6,'2025-07-14 15:08', 95000,N'Bank Transfer', 'TBPAY_006',N'Pending'),
	( 7,'2025-07-21 17:05',115000,N'Credit Card',   'TBPAY_007',N'Completed'),
	( 8,'2025-07-09 09:03', 85000,N'Cash',          'TBPAY_008',N'Completed'),
	( 9,'2025-07-16 09:07', 95000,N'Bank Transfer', 'TBPAY_009',N'Pending'),
	(10,'2025-07-23 11:07',100000,N'Credit Card',   'TBPAY_010',N'Completed'),
	(11,'2025-07-10 16:03',100000,N'Cash',          'TBPAY_011',N'Completed'),
	(12,'2025-07-17 16:05', 95000,N'Credit Card',   'TBPAY_012',N'Completed'),
	(13,'2025-07-24 07:10',110000,N'Bank Transfer', 'TBPAY_013',N'Pending'),
	(14,'2025-07-06 13:03',120000,N'Credit Card',   'TBPAY_014',N'Completed'),
	(15,'2025-07-13 19:05',115000,N'Cash',          'TBPAY_015',N'Completed');
	GO
	-- Insert data into UserReviews
	INSERT INTO UserReviews (UserID, PoolID, Rating, Comment, CreatedAt) VALUES
	(2, 1, 5, N'Great facilities and clean pool!', '2025-05-20 16:00:00'),
	(5, 1, 4, N'Fun for kids, but crowded.', '2025-05-21 17:30:00'),
	(4, 1, 3, N'Needs better heating.', '2025-05-22 11:00:00'),
	(7, 1, 4, N'Beautiful lighting at night!', '2025-05-23 17:00:00'),
	(8, 1, 5, N'Perfect for competitions.', '2025-05-24 12:00:00'),
	(9, 1, 3, N'Needs better maintenance.', '2025-05-24 13:30:00'),
	(11, 1, 5, N'Amazing spa facilities!', '2025-05-24 14:00:00'),
	(12, 1, 4, N'Fun water park, but busy.', '2025-05-24 16:00:00'),
	(13, 1, 4, N'Great for training sessions.', '2025-05-24 18:00:00'),
	(15, 1, 5, N'Eco-friendly and clean!', '2025-05-25 15:00:00'),
	(16, 1, 4, N'Amazing views, but small pool.', '2025-05-25 16:00:00'),
	(17, 1, 3, N'Relaxing, but under maintenance.', '2025-05-25 17:00:00'),
	(18, 1, 5, N'Luxurious and relaxing pool!', '2025-05-26 15:00:00'),
	(20, 1, 4, N'Great views, but chilly water.', '2025-05-26 16:00:00'),
	(19, 1, 3, N'Fun theme, but needs repairs.', '2025-05-26 17:00:00'),
	(2, 1, 1, N'áadasdasd', '2025-05-26 22:02:22.163'), -- Added from file2
	(2, 1, 5, N'dấdad', '2025-05-26 22:02:35.457'), -- Added from file2
	(2, 1, 1, N'asdasd', '2025-05-26 23:14:11.083'), -- Added from file2
	(2, 1, 1, N'awdsadas', '2025-05-26 23:14:15.327'), -- Added from file2
	(2, 1, 1, N'hahahh', '2025-05-26 23:14:22.833'), -- Added from file2
	(2, 1, 1, N'xin chao', '2025-05-26 23:14:51.350'), -- Added from file2
	(2, 1, 1, N'hello', '2025-05-26 23:25:20.343'), -- Added from file2
	(2, 1, 1, N'xin chao', '2025-05-26 23:43:13.010'), -- Added from file2
	(8, 1, 1, N'xin chao', '2025-05-26 23:46:44.283'), -- Added from file2
	(8, 1, 1, N'xin chao', '2025-05-26 23:48:47.837'); -- Added from file2


	Go


	-- Thêm dữ liệu vào bảng Employee với cột Position
	INSERT INTO Employee (UserID, Position, Description, StartDate, EndDate, Attendance, Salary, PoolID, HourlyRate) VALUES
	(3, N'Manager', N'Lifeguard and maintenance staff', '2025-01-01', NULL, 20, 1000.00, 1, NULL),
	(4, null, N'Swim trainer', '2025-02-01', NULL, 22, 1200.00, 2, 100000.00),
	(8, N'Warehouse Management', N'Pool maintenance staff', '2025-03-01', NULL, 18, 900.00, 3, NULL),
	(9, null, N'Swim instructor', '2025-04-01', NULL, 20, 1100.00, 3, 100000.00),
	(12, N'Warehouse Management', N'Lifeguard', '2025-05-01', NULL, 15, 950.00, 4, NULL),
	(13, null, N'Swim coach', '2025-05-15', NULL, 18, 1150.00, 5, 100000.00),
	(16, N'Warehouse Management', N'Maintenance staff', '2025-06-01', NULL, 10, 850.00, 3, NULL),
	(17,null, N'Swim coach', '2025-06-01', NULL, 12, 1100.00, 4, 100000.00),
	(20, null, N'Lifeguard', '2025-06-01', NULL, 8, 900.00, 8, 100000.00),
	(19, N'Manager', N'Swim coach', '2025-06-01', NULL, 10, 1100.00, 10, NULL),
	(21, N'Seller', N'Nhân viên hành chính hồ bơi', '2025-02-05', NULL, 0, 980000, 2, NULL),
	(23, N'Warehouse Management', N'Nhân viên trực ca tối', '2025-02-10', NULL, 0, 990000, 1, NULL),
	(24, N'Warehouse Management', N'Nhân viên cứu hộ', '2025-02-15', NULL, 0, 1020000, 2, NULL),
	(25, N'Warehouse Management', N'Nhân viên bảo trì', '2025-02-20', NULL, 0, 950000, 3, NULL),
	(26, N'Warehouse Management', N'Nhân viên kỹ thuật máy bơm', '2025-02-25', NULL, 0, 970000, 4, NULL),
	(27, N'Seller', N'Nhân viên cứu hộ chính', '2025-03-01', NULL, 0, 1100000, 5, NULL),
	(28, N'Seller', N'Nhân viên hỗ trợ khách hàng', '2025-03-05', NULL, 0, 930000, 6, NULL),
	(29, N'Warehouse Management', N'Nhân viên cứu hộ khẩn cấp', '2025-03-10', NULL, 0, 1050000, 3, NULL),
	(30, N'Seller', N'Huấn luyện viên thể lực', '2025-03-15', NULL, 0, 1200000, 1, NULL),
	(31, N'Manager', N'Giám sát hồ bơi khu vực B', '2025-03-20', NULL, 0, 1250000, 2, NULL);
	GO

	-- Insert data into Notifications
	INSERT INTO Notifications (UserID, Title, Message, IsRead, CreatedAt) VALUES
	(2, 'Order Confirmation', 'Your order for swim goggles has been confirmed.', 1, '2025-05-20 12:30:00'),
	(2, 'Event Reminder', 'Join us for Kids Swim Day on June 15!', 0, '2025-05-21 08:00:00'),
	(2, 'Schedule Update', 'Your training session is scheduled for May 25.', 1, '2025-05-22 09:00:00'),
	(2, 'Order Confirmation', 'Your order for a swim towel has been confirmed.', 1, '2025-05-23 14:30:00'),
	(2, 'Event Reminder', 'Join us for the Swim Championship on June 20!', 0, '2025-05-24 08:00:00'),
	(2, 'Schedule Update', 'Your coaching session is scheduled for May 28.', 1, '2025-05-24 09:30:00'),
	(2, 'Order Confirmation', 'Your order for earplugs has been confirmed.', 1, '2025-05-24 13:30:00'),
	(2, 'Event Reminder', 'Join us for Aqua Fun Day on July 1!', 0, '2025-05-24 15:30:00'),
	(2, 'Schedule Update', 'Your coaching session is scheduled for May 31.', 1, '2025-05-24 17:30:00'),
	(2, 'Order Confirmation', 'Your order for a swim vest has been confirmed.', 1, '2025-05-25 10:30:00'),
	(2, 'Event Reminder', 'Join us for Rooftop Swim Party on July 20!', 0, '2025-05-25 11:00:00'),
	(2, 'Schedule Update', 'Your coaching session is scheduled for June 3.', 1, '2025-05-25 13:30:00'),
	(2, 'Order Confirmation', 'Your order for a swim buoy has been confirmed.', 1, '2025-05-26 10:30:00'),
	(2, 'Event Reminder', 'Join us for Tropical Pool Party on August 15!', 0, '2025-05-26 11:00:00'),
	(2, 'Schedule Update', 'Your coaching session is scheduled for June 6.', 1, '2025-05-26 13:30:00');

	-- Insert data into UserPackages
	INSERT INTO UserPackages (UserID, PoolID, PackageID, StartDate, EndDate, IsActive, PaymentStatus) VALUES
	(2, 1, 2, '2025-05-25', DATEADD(day, 30, '2025-05-25'), 1, N'Completed'),
	(5, 2 ,1, '2025-05-20', DATEADD(day, 7, '2025-05-20'), 0, N'Completed'),
	(7, 3,3, '2025-01-01', DATEADD(day, 365, '2025-01-01'), 1, N'Completed'),
	(10,4 ,4, '2025-05-15', DATEADD(day, 90, '2025-05-15'), 1, N'Completed'),
	(11, 5,1, '2025-05-25', DATEADD(day, 7, '2025-05-25'), 1, N'Pending'),
	(14, 6,2, '2025-05-10', DATEADD(day, 30, '2025-05-10'), 1, N'Completed'),
	(18, 7,3, '2025-03-01', DATEADD(day, 365, '2025-03-01'), 1, N'Completed'),
	(21, 8,1, '2025-05-24', DATEADD(day, 7, '2025-05-24'), 1, N'Completed');

	-- Insert data into TrainerReviews
	INSERT INTO TrainerReviews (UserID, TrainerID, Rating, Comment, CreatedAt) VALUES
	(5, 4, 5, N'Excellent', '2025-05-25'),
	(6, 4, 4, N'Very enthusiastic', '2025-05-26'),
	(7, 4, 5, N'Clear instructions', '2025-05-27'),
	(10, 9, 3, N'Average', '2025-05-28'),
	(11, 9, 4, N'Effective training', '2025-05-29'),
	(14, 9, 5, N'Enthusiastic and professional', '2025-05-30'),
	(18, 13, 4, N'Good, curriculum needs improvement', '2025-05-31'),
	(11, 13, 5, N'Very good', '2025-06-01'),
	(5, 17, 5, N'Outstanding', '2025-06-02'),
	(6, 17, 4, N'Stable and friendly', '2025-06-03'),
	(7, 13, 5, N'Professional and supportive', '2025-06-04'),
	(10, 4, 4, N'Motivating and well-prepared', '2025-06-05'),
	(14, 17, 5, N'Great experience with the trainer', '2025-06-06'),
	(18, 9, 3, N'Needs to improve communication', '2025-06-07'),
	(6, 13, 5, N'Excellent guidance and encouragement', '2025-06-08'),
	(5, 9, 4, N'Solid sessions, good pacing', '2025-06-09'),
	(11, 4, 5, N'Always energetic and inspiring', '2025-06-10'),
	(14, 13, 4, N'Helpful with customized plans', '2025-06-11'),
	(10, 17, 5, N'Makes workouts fun and effective', '2025-06-12'),
	(7, 9, 3, N'Decent trainer, can be more engaging', '2025-06-13');

	-- Insert data into PoolPackages
	-- Gói Weekly Pass - Standard (PackageID = 1)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(1, 1, NULL, 1, N'Gói tuần có sẵn tại Sunset Pool'),
	(2, 1, NULL, 1, N'Gói tuần có sẵn tại Blue Wave Pool'),
	(5, 1, NULL, 1, N'Gói tuần có sẵn tại Crystal Lake Pool');

	-- Gói Monthly Unlimited - Basic (PackageID = 2)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(1, 2, NULL, 1, N'Gói tháng không giới hạn có sẵn tại Sunset Pool'),
	(2, 2, NULL, 1, N'Gói tháng không giới hạn có sẵn tại Blue Wave Pool');

	-- Gói Annual Gold Pass - Premium (PackageID = 3)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(7, 3, NULL, 1, N'Gói vàng hàng năm tại Golden Sun Pool (Bể bơi cao cấp)'),
	(13, 3, NULL, 1, N'Gói vàng hàng năm tại Pearl Pool (Bể bơi sang trọng)');

	-- Gói 3-Month Training Pass (PackageID = 4)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(9, 4, NULL, 1, N'Gói 3 tháng luyện tập tại Silver Wave Pool (Bể bơi đào tạo)'),
	(5, 4, NULL, 1, N'Gói 3 tháng luyện tập tại Crystal Lake Pool');

	-- Gói Single Day Pass - Weekday (PackageID = 5)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(1, 5, NULL, 1, N'Vé một ngày các ngày trong tuần tại Sunset Pool'),
	(2, 5, NULL, 1, N'Vé một ngày các ngày trong tuần tại Blue Wave Pool');

	-- Gói Weekend Family Pass (PackageID = 6)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(2, 6, NULL, 1, N'Gói gia đình cuối tuần tại Blue Wave Pool'),
	(6, 6, NULL, 0, N'Gói gia đình cuối tuần tại Ocean Breeze Pool (Tạm thời không khả dụng)'),
	(15, 6, NULL, 1, N'Gói gia đình cuối tuần tại Tropical Oasis Pool');

	-- Gói Summer Swim Fest Special (PackageID = 7)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(1, 7, 1, 1, N'Gói đặc biệt cho Summer Swim Fest tại Sunset Pool'), -- EventID 1: Summer Swim Fest
	(5, 7, 5, 1, N'Gói đặc biệt cho Swim Championship tại Carystal Lake Pool'); -- EventID 5: Swim Championship

	-- Gói Kids Swim Day Pass (PackageID = 8)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(2, 8, 2, 1, N'Vé dành cho trẻ em trong Kids Swim Day tại Blue Wave Pool'), -- EventID 2: Kids Swim Day
	(8, 8, 8, 1, N'Vé dành cho trẻ em trong Aqua Fun Day tại Aqua Park Pool'); -- EventID 8: Aqua Fun Day

	-- Gói Night Swim Party Access (PackageID = 9)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(3, 9, 3, 1, N'Truy cập tiệc bơi đêm tại Starlight Pool'), -- EventID 3: Night Swim Party
	(4, 9, 4, 1, N'Truy cập tiệc bơi đêm tại Moonlight Pool'), -- EventID 4: Moonlight Swim Night
	(11, 9, 11, 1, N'Truy cập tiệc bơi đêm tại Skyline Pool'); -- EventID 11: Rooftop Swim Party

	-- Gói Luxury Pool Access - 5 Days (PackageID = 10)
	INSERT INTO PoolPackages (PoolID, PackageID, EventID, AvailabilityStatus, Notes) VALUES
	(7, 10, NULL, 1, N'Truy cập 5 ngày bể bơi sang trọng tại Golden Sun Pool'),
	(13, 10, NULL, 1, N'Truy cập 5 ngày bể bơi sang trọng tại Pearl Pool');

	-- Insert data into EventRegistrations
	INSERT INTO EventRegistrations (EventID, UserID, RegisteredAt, Status, Note, FullName, Phone, Email, Address) VALUES
	(1, 1, '2025-06-02', N'Pending', N'Vui lòng sắp xếp chỗ ngồi cho trẻ em.', N'Nguyen Van A', N'0901234567', N'nva@example.com', N'123 Le Loi, Q1, TP.HCM'),
	(2, 4, '2025-06-02', N'Pending', N'Đăng ký cho 2 trẻ em.', N'Tran Thi B', N'0912345678', N'ttb@example.com', N'456 Nguyen Trai, Q5, TP.HCM'),
	(5, 2, '2025-06-02', N'Pending', N'Muốn tham gia thi bơi tự do.', N'Le Van C', N'0923456789', N'lvc@example.com', N'789 Hai Ba Trung, Q3, TP.HCM'),
	(10, 9, '2025-06-02', N'Pending', N'Quan tâm đến hoạt động bảo vệ môi trường.', N'Pham Thi D', N'0934567890', N'ptd@example.com', N'101 Ba Huyen Thanh Quan, Q3, TP.HCM'),
	(13, 1, '2025-06-02', N'Pending', N'Đăng ký tham dự buổi khai trương.', N'Nguyen Van A', N'0901234567', N'nva@example.com', N'123 Le Loi, Q1, TP.HCM');



