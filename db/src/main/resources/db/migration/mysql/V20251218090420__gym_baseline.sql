-- ENSURE THAT TIME ZONE IS SET TO +00 TO STORE TIME IN UTC
-- Is currently problematic with test db
-- SET GLOBAL time_zone = '+00:00';

-- User Management
START TRANSACTION;

CREATE TABLE actors (
  id              binary(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
  type            ENUM('user','system') NOT NULL,
  created_at      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  deactivated_at  datetime(6) NULL
);

-- Object Storage

CREATE TABLE object_storage (
  id          binary(16) PRIMARY KEY,
  bucket      varchar(255) NOT NULL,
  file_key    varchar(1024) NOT NULL,
  name        varchar(255) NOT NULL,
  file_size   BIGINT UNSIGNED NOT NULL,
  mime_type   varchar(255) NOT NULL,
  tags        varchar(255) NOT NULL,
  status      int NOT NULL,
  created_by  binary(16) NOT NULL,
  updated_by  binary(16) NOT NULL,
  created_at  datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at  datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT object_storage_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT object_storage_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Branch Management

CREATE TABLE branch (
  id              binary(16) PRIMARY KEY,
  name            varchar(255) UNIQUE NOT NULL,
  address         text NOT NULL,
  longitude       varchar(255) NOT NULL,
  latitude        varchar(255) NOT NULL,
  -- Not yet final
  status          enum('active','closed','fired') NOT NULL,
  profile_picture binary(16) NOT NULL,
  created_by      binary(16) NOT NULL,
  updated_by      binary(16) NOT NULL,
  created_at      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  -- Not sure if i include ang address for uniqueness
  CONSTRAINT branch_ibfk_1 FOREIGN KEY (profile_picture) REFERENCES object_storage (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT branch_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT branch_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- User Management

CREATE TABLE users (
  id          binary(16) PRIMARY KEY,
  actor_id    binary(16) UNIQUE NOT NULL,
  email       varchar(255) UNIQUE NOT NULL,
  password    varchar(255) NOT NULL,
  created_at  datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at  datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT users_ibfk_1 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE employees (
  id              binary(16) PRIMARY KEY,
  user_id         binary(16) UNIQUE DEFAULT NULL,
  actor_id        binary(16) UNIQUE NOT NULL,
  surname         varchar(255) NOT NULL,
  first_name      varchar(255) NOT NULL,
  middle_name     varchar(255) DEFAULT NULL,
  suffix          varchar(10) DEFAULT NULL,
  contact_no      varchar(13) DEFAULT NULL,
  -- Not yet final
  status          enum('employed','retired','fired') NOT NULL,
  profile_picture binary(16) NOT NULL,
  created_at      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT employees_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT employees_ibfk_2 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT employees_ibfk_3 FOREIGN KEY (profile_picture) REFERENCES object_storage (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE employee_objects (
  employee_id binary(16) NOT NULL,
  object_id   binary(16) NOT NULL,
  PRIMARY KEY (employee_id,object_id),
  CONSTRAINT employee_objects_ibfk_1 FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT employee_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE roles (
  id          binary(16) PRIMARY KEY,
  name        varchar(255) UNIQUE NOT NULL,
  description varchar(255) NOT NULL,
  created_by  binary(16) NOT NULL,
  updated_by  binary(16) NOT NULL,
  created_at  datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at  datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT roles_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT roles_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE permissions (
  id 		binary(16) PRIMARY KEY,
  name 		varchar(255) UNIQUE NOT NULL
);

CREATE TABLE role_permissions (
  role_id 			binary(16) NOT NULL,
  permission_id 	binary(16) NOT NULL,
  PRIMARY KEY (role_id,permission_id),
  CONSTRAINT roles_permissions_ibfk_1 FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT roles_permissions_ibfk_2 FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE user_roles (
  user_id binary(16) NOT NULL,
  role_id binary(16) NOT NULL,
  PRIMARY KEY (user_id,role_id),
  CONSTRAINT user_roles_ibfk_1 FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT user_roles_ibfk_2 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

-- Member Management

CREATE TABLE members (
  id 				binary(16) PRIMARY KEY,
  -- Formulate a unique key to prevent duplicate members
  actor_id 			binary(16) UNIQUE NOT NULL,
  surname			varchar(255) NOT NULL,
  first_name 		varchar(255) NOT NULL,
  middle_name 		varchar(255) DEFAULT NULL,
  suffix 			varchar(10) DEFAULT NULL,
  -- Not yet final
  status 			enum('active','inactive') NOT NULL,
  profile_picture 	binary(16) NOT NULL,
  created_by 		binary(16) NOT NULL,
  updated_by 		binary(16) NOT NULL,
  created_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT members_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT members_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT members_ibfk_3 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT members_ibfk_4 FOREIGN KEY (profile_picture) REFERENCES object_storage (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE member_objects (
  member_id binary(16) NOT NULL,
  object_id binary(16) NOT NULL,
  PRIMARY KEY (member_id,object_id),
  CONSTRAINT member_objects_ibfk_1 FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT member_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE progress (
  id 		binary(16) PRIMARY KEY,
  name 		varchar(255) UNIQUE NOT NULL
);

CREATE TABLE progress_options (
  id 			binary(16) PRIMARY KEY,
  progress_id 	binary(16) NOT NULL,
  name 			varchar(255) UNIQUE NOT NULL,
  CONSTRAINT progress_options_ibfk_1 FOREIGN KEY (progress_id) REFERENCES progress (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE member_progress (
  id 					binary(16) PRIMARY KEY,
  actor_id 				binary(16) NOT NULL,
  progress_option_id 	binary(16) NOT NULL,
  branch_id 			binary(16) NOT NULL,
  remarks 				varchar(255) NOT NULL,
  -- Not yet final
  status				enum('ongoing','canceled') NOT NULL,
  created_by 			binary(16) NOT NULL,
  updated_by 			binary(16) NOT NULL,
  created_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  completed_at 			datetime(6) DEFAULT NULL,
  CONSTRAINT member_progress_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_progress_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_progress_ibfk_3 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_progress_ibfk_4 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE attendance_types (
  id 		binary(16) PRIMARY KEY,
  name 		varchar(255) UNIQUE NOT NULL
);

CREATE TABLE attendance (
  id 					binary(16) PRIMARY KEY,
  actor_id 				binary(16) NOT NULL,
  branch_id 			binary(16) NOT NULL,
  attendance_type_id 	binary(16) NOT NULL,
  remarks 				varchar(255) NOT NULL,
  -- Not yet final
  type 					enum('in','out','undecided') NOT NULL,
  created_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT attendance_ibfk_1 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT attendance_ibfk_2 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT attendance_ibfk_3 FOREIGN KEY (attendance_type_id) REFERENCES attendance_types (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE billing_cycles (
  id 				binary(16) PRIMARY KEY,
  name 				varchar(255) NOT NULL,
  intervals 		enum('daily','weekly','monthly','yearly') NOT NULL,
  interval_count 	int NOT NULL,
  grace_period 		int NOT NULL,
  created_by 		binary(16) NOT NULL,
  updated_by 		binary(16) NOT NULL,
  created_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT billing_cycles_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT billing_cycles_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE subscriptions (
  id 					binary(16) PRIMARY KEY,
  billing_cycle_id 		binary(16) NOT NULL,
  name 					varchar(255) NOT NULL,
  amount 				decimal(10,2) NOT NULL,
  created_by 			binary(16) NOT NULL,
  updated_by 			binary(16) NOT NULL,
  created_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT subscriptions_ibfk_1 FOREIGN KEY (billing_cycle_id) REFERENCES billing_cycles (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT subscriptions_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT subscriptions_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE subscriptions_availed (
  id 				binary(16) PRIMARY KEY,
  subscription_id 	binary(16) NOT NULL,
  name 				varchar(255) NOT NULL,
  amount 			decimal(10,2) NOT NULL,
  intervals 		enum('daily','weekly','monthly','yearly') NOT NULL,
  interval_count 	int NOT NULL,
  grace_period 	int NOT NULL,
  CONSTRAINT subscriptions_availed_ibfk_1 FOREIGN KEY (subscription_id) REFERENCES subscriptions (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE member_subscriptions (
  id 						binary(16) PRIMARY KEY,
  actor_id 					binary(16) NOT NULL,
  subscription_availed_id 	binary(16) NOT NULL,
  branch_id 				binary(16) NOT NULL,
  start_date 				date NOT NULL,
  end_date					date NULL,
  -- Not yet sure what to put
  status 					enum('active','paused','canceled') NOT NULL,
  created_by 				binary(16) NOT NULL,
  updated_by 				binary(16) NOT NULL,
  created_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT member_subscriptions_ibfk_1 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_subscriptions_ibfk_2 FOREIGN KEY (subscription_availed_id) REFERENCES subscriptions_availed (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_subscriptions_ibfk_3 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_subscriptions_ibfk_4 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_subscriptions_ibfk_5 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Payment history and Billing

CREATE TABLE invoices (
  id 						binary(16) PRIMARY KEY,
  actor_id 					binary(16) NOT NULL,
  subscription_availed_id 	binary(16) NOT NULL,
  branch_id 				binary(16) NOT NULL,
  -- Not yet sure what to put
  status 					enum('draft','issued','paid','overnight') NOT NULL,
  subtotal 					decimal(10,2) NOT NULL,
  discount 					decimal(10,2) NULL,
  convenience_fee 			decimal(10,2) NULL,
  total 					decimal(10,2) NOT NULL,
  due_date 					datetime(6) NOT NULL,
  issued_at					datetime(6) NOT NULL,
  system_generated 			BOOLEAN NOT NULL DEFAULT TRUE,
  created_by 				binary(16) NOT NULL,
  updated_by 				binary(16) NOT NULL,
  created_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT invoices_ibfk_1 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT invoices_ibfk_2 FOREIGN KEY (subscription_availed_id) REFERENCES subscriptions_availed (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT invoices_ibfk_3 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT invoices_ibfk_4 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT invoices_ibfk_5 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE payment_methods (
  id 	binary(16) PRIMARY KEY,
  name 	varchar(255) UNIQUE NOT NULL
);

CREATE TABLE payments (
  id 					binary(16) PRIMARY KEY,
  invoice_id 			binary(16) NOT NULL,
  -- Not yet sure what to put
  status 				enum('pending','failed','paid','refund') NOT NULL,
  payment_method_id 	binary(16) NOT NULL,
  amount 				decimal(10,2) NOT NULL,
  paid_at 				datetime(6) NOT NULL,
  failure_reason 		varchar(255) NULL,
  created_by 			binary(16) NOT NULL,
  updated_by 			binary(16) NOT NULL,
  created_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT payment_ibfk_1 FOREIGN KEY (invoice_id) REFERENCES invoices (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT payment_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT payment_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT payment_ibfk_4 FOREIGN KEY (payment_method_id) REFERENCES payment_methods (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE ledgers (
  id 						binary(16) PRIMARY KEY,
  actor_id 					binary(16) NOT NULL,
  subscription_availed_id 	binary(16) NOT NULL,
  branch_id 				binary(16) NOT NULL,
  invoice_id 				binary(16) NOT NULL,
  payment_id 				binary(16) NOT NULL,
  -- Not yet sure what to put
  entry_type 				enum('invoice','payment','refund') NOT NULL,
  subscription_name 		varchar(255) NOT NULL,
  branch_name 				varchar(255) NOT NULL,
  intervals 				varchar(255) NOT NULL,
  interval_count 			int NOT NULL,
  grace_period 				int NOT NULL,
  amount 					decimal(10,2) NOT NULL,
  payment_method 			varchar(255) NOT NULL,
  created_by 				binary(16) NOT NULL,
  created_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  CONSTRAINT ledgers_ibfk_1 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT ledgers_ibfk_2 FOREIGN KEY (subscription_availed_id) REFERENCES subscriptions_availed (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT ledgers_ibfk_3 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT ledgers_ibfk_4 FOREIGN KEY (invoice_id) REFERENCES invoices (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT ledgers_ibfk_5 FOREIGN KEY (payment_id) REFERENCES payments (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT ledgers_ibfk_6 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Branch Management

CREATE TABLE branch_personnel (
  id 			binary(16) PRIMARY KEY,
  actor_id 		binary(16) NOT NULL,
  branch_id 	binary(16) NOT NULL,
  -- Not final
  status 		enum('active','moved') NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT branch_personnel_ibfk_1 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT branch_personnel_ibfk_2 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT branch_personnel_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT branch_personnel_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Asset Tracking

CREATE TABLE asset_categories (
  id 	binary(16) PRIMARY KEY,
  name 	varchar(255) UNIQUE NOT NULL
);

CREATE TABLE maintenance_schedules (
  id 			binary(16) PRIMARY KEY,
  start_date 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  intervals 	enum('daily','weekly','monthly','yearly') NOT NULL,
  interval_count int NOT NULL
);

CREATE TABLE assets (
  id 						binary(16) PRIMARY KEY,
  branch_id 				binary(16) NOT NULL,
  maintenance_schedule_id 	binary(16) NOT NULL,
  asset_category_id 		binary(16) NOT NULL,
  name 						varchar(255) NOT NULL,
  manufactured_date 		datetime(6) NULL,
  end_of_life 				datetime(6) NULL,
  remarks 					varchar(255) NOT NULL,
  -- Not final
  created_by 				binary(16) NOT NULL,
  updated_by 				binary(16) NOT NULL,
  created_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at			 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT assets_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT assets_ibfk_2 FOREIGN KEY (maintenance_schedule_id) REFERENCES maintenance_schedules (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT assets_ibfk_3 FOREIGN KEY (asset_category_id) REFERENCES asset_categories (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT assets_ibfk_4 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT assets_ibfk_5 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE asset_objects (
  asset_id 	binary(16) NOT NULL,
  object_id	binary(16) NOT NULL,
  PRIMARY KEY (asset_id,object_id),
  CONSTRAINT asset_objects_ibfk_1 FOREIGN KEY (asset_id) REFERENCES assets (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT asset_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE asset_maintenance (
  id 			binary(16) PRIMARY KEY,
  asset_id 		binary(16) NOT NULL,
  status 		enum('due','done','skipped','stopped') NOT NULL,
  description 	varchar(255) NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT asset_maintenance_ibfk_1 FOREIGN KEY (asset_id) REFERENCES assets (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE asset_maintenance_objects (
  asset_maintenance_id 	binary(16) NOT NULL,
  object_id 			binary(16) NOT NULL,
  PRIMARY KEY (asset_maintenance_id,object_id),
  CONSTRAINT asset_maintenance_objects_ibfk_1 FOREIGN KEY (asset_maintenance_id) REFERENCES asset_maintenance (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE supplies (
  id 			binary(16) PRIMARY KEY,
  branch_id 	binary(16) NOT NULL,
  name 		varchar(255) NOT NULL,
  -- derived from the supplies_log
  quantity 	int NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT supplies_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE supplies_logs (
  id 			binary(16) PRIMARY KEY,
  supplies_id 	binary(16) NOT NULL,
  name 			varchar(255) NOT NULL,
  quantity 		int NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT supplies_logs_ibfk_1 FOREIGN KEY (supplies_id) REFERENCES supplies (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_logs_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_logs_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Expense Tracking

CREATE TABLE asset_maintenance_expenses (
  id 						binary(16) PRIMARY KEY,
  branch_id 				binary(16) NOT NULL,
  asset_maintenance_id 		binary(16) NOT NULL,
  amount 					decimal(10,2) NOT NULL,
  paid_at 					datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by 				binary(16) NOT NULL,
  updated_by 				binary(16) NOT NULL,
  created_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT asset_maintenance_expenses_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_expenses_ibfk_2 FOREIGN KEY (asset_maintenance_id) REFERENCES asset_maintenance (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_expenses_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_expenses_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE asset_maintenance_expense_objects (
  asset_maintenance_expense_id 		binary(16) NOT NULL,
  object_id 						binary(16) NOT NULL,
  PRIMARY KEY (asset_maintenance_expense_id,object_id),
  CONSTRAINT asset_maintenance_expense_objects_ibfk_1 FOREIGN KEY (asset_maintenance_expense_id) REFERENCES asset_maintenance_expenses (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_expense_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE salary_expenses (
  id 			binary(16) PRIMARY KEY,
  branch_id 	binary(16) NOT NULL,
  -- Employee
  actor_id 		binary(16) NOT NULL,
  salary_type 	enum('full','partial','advanced') NOT NULL,
  amount 		decimal(10,2) NOT NULL,
  period 		date NOT NULL,
  paid_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT salary_expenses_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT salary_expenses_ibfk_2 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT salary_expenses_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT salary_expenses_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE salary_expense_objects (
  salary_expense_id binary(16) NOT NULL,
  object_id 		binary(16) NOT NULL,
  PRIMARY KEY (salary_expense_id,object_id),
  CONSTRAINT salary_expense_objects_ibfk_1 FOREIGN KEY (salary_expense_id) REFERENCES salary_expenses (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT salary_expense_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE utility_types (
  id 	binary(16) PRIMARY KEY,
  name 	varchar(255) UNIQUE NOT NULL
);

CREATE TABLE utility_expenses (
  id 				binary(16) PRIMARY KEY,
  branch_id 		binary(16) NOT NULL,
  utility_type_id 	binary(16) NOT NULL,
  meter 			varchar(255) NOT NULL,
  amount 			decimal(10,2) NOT NULL,
  period 			date NOT NULL,
  paid_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by 		binary(16) NOT NULL,
  updated_by 		binary(16) NOT NULL,
  created_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT utility_expenses_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT utility_expenses_ibfk_2 FOREIGN KEY (utility_type_id) REFERENCES utility_types (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT utility_expenses_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT utility_expenses_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE utility_expense_objects (
  utility_expense_id 	binary(16) NOT NULL,
  object_id 			binary(16) NOT NULL,
  PRIMARY KEY (utility_expense_id,object_id),
  CONSTRAINT utility_expense_objects_ibfk_1 FOREIGN KEY (utility_expense_id) REFERENCES utility_expenses (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT utility_expense_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE other_expense_types (
  id 		binary(16) PRIMARY KEY,
  name 		varchar(255) UNIQUE NOT NULL
);

CREATE TABLE other_expenses (
  id 					binary(16) PRIMARY KEY,
  branch_id 			binary(16) NOT NULL,
  other_expense_id 		binary(16) NOT NULL,
  amount 				decimal(10,2) NOT NULL,
  paid_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by 			binary(16) NOT NULL,
  updated_by 			binary(16) NOT NULL,
  created_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT other_expenses_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT other_expenses_ibfk_2 FOREIGN KEY (other_expense_id) REFERENCES other_expense_types (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT other_expenses_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT other_expenses_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE other_expense_objects (
  other_expense_id 	binary(16) NOT NULL,
  object_id			binary(16) NOT NULL,
  PRIMARY KEY (other_expense_id,object_id),
  CONSTRAINT other_expense_objects_ibfk_1 FOREIGN KEY (other_expense_id) REFERENCES other_expenses (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT other_expense_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE asset_expenses (
  id 			binary(16) PRIMARY KEY,
  branch_id 	binary(16) NOT NULL,
  asset_id 		binary(16) NOT NULL,
  amount 		decimal(10,2) NOT NULL,
  paid_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT asset_expenses_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_expenses_ibfk_2 FOREIGN KEY (asset_id) REFERENCES assets (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_expenses_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_expenses_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE asset_expense_objects (
  asset_expense_id 	binary(16) NOT NULL,
  object_id 		binary(16) NOT NULL,
  PRIMARY KEY (asset_expense_id,object_id),
  CONSTRAINT asset_expense_objects_ibfk_1 FOREIGN KEY (asset_expense_id) REFERENCES asset_expenses (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT asset_expense_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE supplies_expenses (
  id 				binary(16) PRIMARY KEY,
  branch_id 		binary(16) NOT NULL,
  supplies_log_id 	binary(16) NOT NULL,
  amount 			decimal(10,2) NOT NULL,
  paid_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by 		binary(16) NOT NULL,
  updated_by 		binary(16) NOT NULL,
  created_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT supplies_expenses_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_expenses_ibfk_2 FOREIGN KEY (supplies_log_id) REFERENCES supplies_logs (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_expenses_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_expenses_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE supplies_expense_objects (
  supplies_expense_id 	binary(16) NOT NULL,
  object_id 			binary(16) NOT NULL,
  PRIMARY KEY (supplies_expense_id,object_id),
  CONSTRAINT supplies_expense_objects_ibfk_1 FOREIGN KEY (supplies_expense_id) REFERENCES supplies_expenses (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT supplies_expense_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

-- Report Tracking System

CREATE TABLE report_types (
  id 	binary(16) PRIMARY KEY,
  name 	varchar(255) UNIQUE NOT NULL
);

CREATE TABLE reports (
  id 				binary(16) PRIMARY KEY,
  branch_id 		binary(16) NOT NULL,
  actor_id 			binary(16) NOT NULL,
  report_type_id 	binary(16) NOT NULL,
  description 		varchar(255) NOT NULL,
  occurred_at 		datetime(6) NULL,
  created_by 		binary(16) NOT NULL,
  updated_by 		binary(16) NOT NULL,
  created_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT reports_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT reports_ibfk_2 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT reports_ibfk_3 FOREIGN KEY (report_type_id) REFERENCES report_types (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT reports_ibfk_4 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT reports_ibfk_5 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE report_objects (
  report_id binary(16) NOT NULL,
  object_id binary(16) NOT NULL,
  PRIMARY KEY (report_id,object_id),
  CONSTRAINT reports_object_ibfk_1 FOREIGN KEY (report_id) REFERENCES reports (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT reports_object_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

COMMIT;