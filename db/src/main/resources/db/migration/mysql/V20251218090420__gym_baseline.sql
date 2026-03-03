-- ENSURE THAT TIME ZONE IS SET TO +00 TO STORE TIME IN UTC
-- Is currently problematic with test db
-- SET GLOBAL time_zone = '+00:00';

-- User Management
START TRANSACTION;

CREATE TABLE actors (
  id              binary(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID())),
  type            varchar(255) NOT NULL,
  status          varchar(255) NOT NULL,
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
  status          varchar(255) NOT NULL,
  profile_picture binary(16) NULL,
  created_by      binary(16) NOT NULL,
  updated_by      binary(16) NOT NULL,
  created_at      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  -- Not sure if i include ang address for uniqueness
  CONSTRAINT branch_ibfk_1 FOREIGN KEY (profile_picture) REFERENCES object_storage (id) ON DELETE SET NULL,
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
  contact_no      varchar(25) DEFAULT NULL,
  -- Not yet final
  status          varchar(255) NOT NULL,
  profile_picture binary(16) NULL,
  created_at      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at      datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT employees_ibfk_1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT employees_ibfk_2 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT employees_ibfk_3 FOREIGN KEY (profile_picture) REFERENCES object_storage (id) ON DELETE SET NULL
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
  description varchar(255) NULL,
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
  actor_id 			binary(16) UNIQUE NOT NULL,
  surname			varchar(255) NOT NULL,
  first_name 		varchar(255) NOT NULL,
  middle_name 		varchar(255) DEFAULT NULL,
  suffix 			varchar(10) DEFAULT NULL,
  -- Not yet final
  status 			varchar(255) NOT NULL,
  profile_picture 	binary(16) NULL,
  created_by 		binary(16) NOT NULL,
  updated_by 		binary(16) NOT NULL,
  created_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 		datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  -- Not sure to include middle_name and suffix as they are nullable
  CONSTRAINT uk_name UNIQUE (surname, first_name),
  CONSTRAINT members_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT members_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT members_ibfk_3 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT members_ibfk_4 FOREIGN KEY (profile_picture) REFERENCES object_storage (id) ON DELETE SET NULL
);

CREATE TABLE member_objects (
  member_id binary(16) NOT NULL,
  object_id binary(16) NOT NULL,
  PRIMARY KEY (member_id,object_id),
  CONSTRAINT member_objects_ibfk_1 FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT member_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE progress_options (
  id 			binary(16) PRIMARY KEY,
  name 			varchar(255) UNIQUE NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT progress_options_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT progress_options_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE progress (
  id 					binary(16) PRIMARY KEY,
  name 					varchar(255) UNIQUE NOT NULL,
  progress_option_id	binary(16) NOT NULL,
  created_by 			binary(16) NOT NULL,
  updated_by 			binary(16) NOT NULL,
  created_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT progress_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT progress_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT progress_ibfk_3 FOREIGN KEY (progress_option_id) REFERENCES progress_options (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE member_progress (
  id 					binary(16) PRIMARY KEY,
  actor_id 				binary(16) NOT NULL,
  progress_option_id 	binary(16) NOT NULL,
  progress_id		 	binary(16) NOT NULL,
  branch_id 			binary(16) NOT NULL,
  remarks 				text NULL,
  -- Not yet final
  status				varchar(255) NOT NULL,
  created_by 			binary(16) NOT NULL,
  updated_by 			binary(16) NOT NULL,
  created_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  completed_at 			datetime(6) DEFAULT NULL,
  -- Does not include branch as it is just an indicator of initial creation
  -- Might include when logic requires it
  CONSTRAINT uk_actor_progress UNIQUE (actor_id, progress_option_id),
  CONSTRAINT member_progress_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_progress_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_progress_ibfk_3 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_progress_ibfk_4 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_progress_ibfk_5 FOREIGN KEY (progress_option_id) REFERENCES progress_options (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_progress_ibfk_6 FOREIGN KEY (progress_id) REFERENCES progress (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE member_progress_history (
  id 					binary(16) PRIMARY KEY,
  member_progress_id	binary(16) NOT NULL,
  progress_id			binary(16) NOT NULL,
  changed_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  INDEX changed_at (changed_at),
  CONSTRAINT member_progress_history_ibfk_1 FOREIGN KEY (member_progress_id) REFERENCES member_progress (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT member_progress_history_ibfk_2 FOREIGN KEY (progress_id) REFERENCES progress (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);


CREATE TABLE attendance (
  id 					binary(16) PRIMARY KEY,
  actor_id 				binary(16) NOT NULL,
  branch_id 			binary(16) NOT NULL,
  source 				varchar(255) NOT NULL,
  type 					varchar(255) NOT NULL,
  remarks 				varchar(255) NULL,
  recorded_at 			datetime(6) NOT NULL,
  -- Not yet final
  created_by 			binary(16) NOT NULL,
  updated_by 			binary(16) NOT NULL,
  created_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  INDEX recorded_at (recorded_at),
  CONSTRAINT attendance_ibfk_1 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT attendance_ibfk_2 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT attendance_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT attendance_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE billing_cycles (
  id 				binary(16) PRIMARY KEY,
  name 				varchar(255) UNIQUE NOT NULL,
  intervals 		varchar(255) NOT NULL,
  interval_count 	int NOT NULL,
  grace_period_days int NOT NULL,
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
  name 					varchar(255) UNIQUE NOT NULL,
  description 			text NULL,
  amount 				decimal(10,2) NOT NULL,
  created_by 			binary(16) NOT NULL,
  updated_by 			binary(16) NOT NULL,
  created_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 			datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT uk_billing_name UNIQUE (billing_cycle_id, name),
  CONSTRAINT subscriptions_ibfk_1 FOREIGN KEY (billing_cycle_id) REFERENCES billing_cycles (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT subscriptions_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT subscriptions_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE subscriptions_availed (
  id 				binary(16) PRIMARY KEY,
  subscription_id 	binary(16) NOT NULL,
  name 				varchar(255) NOT NULL,
  amount 			decimal(10,2) NOT NULL,
  intervals 		varchar(255) NOT NULL,
  interval_count 	int NOT NULL,
  grace_period_days int NOT NULL,
  CONSTRAINT uk_subscription_availed UNIQUE (subscription_id, name, amount, intervals, interval_count, grace_period_days),
  CONSTRAINT subscriptions_availed_ibfk_1 FOREIGN KEY (subscription_id) REFERENCES subscriptions (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE member_subscriptions (
  id 						binary(16) PRIMARY KEY,
  actor_id 					binary(16) NOT NULL,
  subscription_availed_id 	binary(16) NOT NULL,
  branch_id 				binary(16) NOT NULL,
  start_date 				datetime(6) NOT NULL,
  end_date					datetime(6) NULL,
  -- Not yet sure what to put
  status 					varchar(255) NOT NULL,
  created_by 				binary(16) NOT NULL,
  updated_by 				binary(16) NOT NULL,
  created_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  INDEX start_date (start_date),
  INDEX end_date (end_date),
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
  member_subscription_id 	binary(16) NOT NULL,
  -- Not yet sure what to put
  status 					varchar(255) NOT NULL,
  subtotal 					decimal(10,2) NOT NULL,
  discount 					decimal(10,2) NULL,
  convenience_fee 			decimal(10,2) NULL,
  total 					decimal(10,2) NOT NULL,
  due_date 					datetime(6) NOT NULL,
  grace_period_date 		datetime(6) NOT NULL,
  issued_at					datetime(6) NOT NULL,
  system_generated 			BOOLEAN NOT NULL DEFAULT TRUE,
  created_by 				binary(16) NOT NULL,
  updated_by 				binary(16) NOT NULL,
  created_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  INDEX due_date (due_date),
  INDEX grace_period_date (grace_period_date),
  CONSTRAINT invoices_ibfk_1 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT invoices_ibfk_2 FOREIGN KEY (subscription_availed_id) REFERENCES subscriptions_availed (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT invoices_ibfk_3 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT invoices_ibfk_4 FOREIGN KEY (member_subscription_id) REFERENCES member_subscriptions (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT invoices_ibfk_5 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT invoices_ibfk_6 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE payment_methods (
  id 			binary(16) PRIMARY KEY,
  name 			varchar(255) UNIQUE NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT payment_methods_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT payment_methods_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE payment_method_objects (
  payment_method_id binary(16) NOT NULL,
  object_id 		binary(16) NOT NULL,
  PRIMARY KEY (payment_method_id,object_id),
  CONSTRAINT payment_method_objects_ibfk_1 FOREIGN KEY (payment_method_id) REFERENCES payment_methods (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT payment_method_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE payments (
  id 					binary(16) PRIMARY KEY,
  invoice_id 			binary(16) NOT NULL,
  reference_num			varchar(255) NULL,
  -- Not yet sure what to put
  status 				varchar(255) NOT NULL,
  payment_method_id 	binary(16) NOT NULL,
  amount 				decimal(10,2) NOT NULL,
  paid_at 				datetime(6) NULL,
  failure_reason 		text NULL,
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
  entry_type 				varchar(255) NOT NULL,
  subscription_name 		varchar(255) NOT NULL,
  branch_name 				varchar(255) NOT NULL,
  intervals 				varchar(255) NOT NULL,
  interval_count 			int NOT NULL,
  grace_period_days 		int NOT NULL,
  amount 					decimal(10,2) NOT NULL,
  payment_method_name 		varchar(255) NOT NULL,
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

CREATE TABLE personnel_roles (
  id          binary(16) PRIMARY KEY,
  name        varchar(255) UNIQUE NOT NULL,
  description varchar(255) NULL,
  created_by  binary(16) NOT NULL,
  updated_by  binary(16) NOT NULL,
  created_at  datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at  datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT personnel_roles_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT personnel_roles_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE branch_personnel (
  id 					binary(16) PRIMARY KEY,
  actor_id 				binary(16) NOT NULL,
  branch_id 			binary(16) NOT NULL,
  personnel_role_id		binary(16) NOT NULL,
  -- Not final
  status 		varchar(255) NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT uk_actor_branch UNIQUE (actor_id, branch_id),
  CONSTRAINT branch_personnel_ibfk_1 FOREIGN KEY (actor_id) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT branch_personnel_ibfk_2 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT branch_personnel_ibfk_3 FOREIGN KEY (personnel_role_id) REFERENCES personnel_roles (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT branch_personnel_ibfk_4 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT branch_personnel_ibfk_5 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

-- Asset Tracking

CREATE TABLE asset_categories (
  id 			binary(16) PRIMARY KEY,
  name 			varchar(255) UNIQUE NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT asset_categories_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_categories_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE assets (
  id 						binary(16) PRIMARY KEY,
  branch_id 				binary(16) NOT NULL,
  asset_category_id 		binary(16) NOT NULL,
  name 						varchar(255) NOT NULL,
  manufactured_date 		datetime(6) NULL,
  end_of_life 				datetime(6) NULL,
  remarks 					text NULL,
  -- Not final
  created_by 				binary(16) NOT NULL,
  updated_by 				binary(16) NOT NULL,
  created_at 				datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at			 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT assets_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT assets_ibfk_2 FOREIGN KEY (asset_category_id) REFERENCES asset_categories (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT assets_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT assets_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE maintenance_schedules (
  id 			            binary(16) PRIMARY KEY,
  asset_id                  binary(16) NOT NULL,
  name                      varchar(255) NOT NULL,
  start_date 	            datetime(6) NOT NULL,
  interval_unit             varchar(255) NOT NULL,
  interval_value            int NOT NULL,
  lead_time_hours           int NOT NULL DEFAULT 0,
  time_to_complete_hours    int NOT NULL DEFAULT 0,
  week_rank                 int NULL,
  day_of_week               int NULL,
  month_of_year             int NULL,
  is_active                 boolean NOT NULL DEFAULT true,
  created_by 				binary(16) NOT NULL,
  updated_by 				binary(16) NOT NULL,
  created_at                datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at                datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT maintenance_schedules_ibfk_1 FOREIGN KEY (asset_id) REFERENCES assets (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT maintenance_schedules_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT maintenance_schedules_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT

);

CREATE TABLE asset_objects (
  asset_id 	binary(16) NOT NULL,
  object_id	binary(16) NOT NULL,
  PRIMARY KEY (asset_id,object_id),
  CONSTRAINT asset_objects_ibfk_1 FOREIGN KEY (asset_id) REFERENCES assets (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT asset_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE asset_maintenance (
  id 			            binary(16) PRIMARY KEY,
  asset_id 		            binary(16) NOT NULL,
  maintenance_schedule_id  binary(16) NOT NULL,
  maintenance_date          datetime(6) NOT NULL,
  due_date                  datetime(6) NOT NULL,
  completion_date           datetime(6) NULL,
  status 		            varchar(255) NOT NULL,
  description 	            text NULL,
  created_by 	            binary(16) NOT NULL,
  updated_by 	            binary(16) NOT NULL,
  created_at 	            datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	            datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT uk_asset_maintenance UNIQUE (asset_id, maintenance_schedule_id, maintenance_date),
  CONSTRAINT asset_maintenance_ibfk_1 FOREIGN KEY (asset_id) REFERENCES assets (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_ibfk_2 FOREIGN KEY (maintenance_schedule_id) REFERENCES maintenance_schedules (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_ibfk_3 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT asset_maintenance_ibfk_4 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
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
  name 			varchar(255) NOT NULL,
  description 	text NULL,
  -- derived from the supplies_log
  quantity 		int NOT NULL DEFAULT 0,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT chk_supplies_quantity_positive CHECK (quantity >= 0),
  CONSTRAINT supplies_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE supplies_objects (
  supplies_id  binary(16) NOT NULL,
  object_id    binary(16) NOT NULL,
  PRIMARY KEY (supplies_id, object_id),
  CONSTRAINT supplies_objects_ibfk_1 FOREIGN KEY (supplies_id) REFERENCES supplies (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT supplies_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE supplies_logs (
  id 			binary(16) PRIMARY KEY,
  supplies_id 	binary(16) NOT NULL,
  name 			varchar(255) NOT NULL,
  remarks		text NULL,
  quantity 		int NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT supplies_logs_ibfk_1 FOREIGN KEY (supplies_id) REFERENCES supplies (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_logs_ibfk_2 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT supplies_logs_ibfk_3 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE supplies_logs_objects (
  supplies_log_id  binary(16) NOT NULL,
  object_id        binary(16) NOT NULL,
  PRIMARY KEY (supplies_log_id, object_id),
  CONSTRAINT supplies_logs_objects_ibfk_1 FOREIGN KEY (supplies_log_id) REFERENCES supplies_logs (id) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT supplies_logs_objects_ibfk_2 FOREIGN KEY (object_id) REFERENCES object_storage (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

-- Triggers to update quantity in 'supplies' table
-- Insert Trigger
DELIMITER //
CREATE TRIGGER tr_supplies_log_after_insert
AFTER INSERT ON supplies_logs
FOR EACH ROW
BEGIN
    UPDATE supplies
    SET quantity = quantity + NEW.quantity
    WHERE id = NEW.supplies_id;
END //
DELIMITER ;

DELIMITER ;

-- Delete Trigger
DELIMITER //
CREATE TRIGGER tr_supplies_log_after_delete
AFTER DELETE ON supplies_logs
FOR EACH ROW
BEGIN
    UPDATE supplies
    SET quantity = quantity - OLD.quantity
    WHERE id = OLD.supplies_id;
END //
DELIMITER ;

-- Update Trigger
DELIMITER //
CREATE TRIGGER tr_supplies_log_after_update
AFTER UPDATE ON supplies_logs
FOR EACH ROW
BEGIN
    IF OLD.quantity <> NEW.quantity OR OLD.supplies_id <> NEW.supplies_id THEN
        UPDATE supplies
        SET quantity = quantity - OLD.quantity
        WHERE id = OLD.supplies_id;

        UPDATE supplies
        SET quantity = quantity + NEW.quantity
        WHERE id = NEW.supplies_id;
    END IF;
END //
DELIMITER ;

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
  salary_type 	varchar(255) NOT NULL,
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
  id 			binary(16) PRIMARY KEY,
  name 			varchar(255) UNIQUE NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT utility_types_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT utility_types_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
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
  id 			binary(16) PRIMARY KEY,
  name 			varchar(255) UNIQUE NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT other_expense_types_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT other_expense_types_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
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
  id 			binary(16) PRIMARY KEY,
  name 			varchar(255) UNIQUE NOT NULL,
  created_by 	binary(16) NOT NULL,
  updated_by 	binary(16) NOT NULL,
  created_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at 	datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  CONSTRAINT report_types_ibfk_1 FOREIGN KEY (created_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT report_types_ibfk_2 FOREIGN KEY (updated_by) REFERENCES actors (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE reports (
  id 				binary(16) PRIMARY KEY,
  branch_id 		binary(16) NOT NULL,
  actor_id 			binary(16) NOT NULL,
  report_type_id 	binary(16) NOT NULL,
  description 		text NULL,
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

-- Analytics

CREATE TABLE branch_financial_summary (
    branch_id 			binary(16) NOT NULL,
    report_year 		int NOT NULL,
    report_quarter 		int NOT NULL,
    report_month 		date NOT NULL,
    total_revenue 		decimal(10,2) DEFAULT 0,
    avg_invoice 		decimal(10,2) DEFAULT 0,
    total_expenses 		decimal(10,2) DEFAULT 0,
    avg_expense 		decimal(10,2) DEFAULT 0,
    PRIMARY KEY (branch_id, report_month),
    INDEX year_quarter (report_year, report_quarter),
    CONSTRAINT branch_financial_summary_ibfk_1 FOREIGN KEY (branch_id) REFERENCES branch (id) ON DELETE CASCADE ON UPDATE RESTRICT
);

CREATE TABLE analytics_metadata (
    report_name 		varchar(255) PRIMARY KEY,
    last_refreshed_at	datetime(6) NOT NULL
);

DELIMITER //

CREATE PROCEDURE refresh_branch_financial_summary()
BEGIN
    DECLARE current_refresh_time DATETIME DEFAULT NOW();

    START TRANSACTION;

    DELETE FROM branch_financial_summary;

    INSERT INTO branch_financial_summary (
        branch_id, report_year, report_quarter, report_month,
        total_revenue, avg_invoice, total_expenses, avg_expense
    )
    SELECT
        combined.branch_id,
        combined.r_year,
        combined.r_quarter,
        combined.r_month,
        COALESCE(SUM(combined.rev), 0) as total_revenue,
        COALESCE(AVG(combined.rev), 0) as avg_invoice,
        COALESCE(SUM(combined.exp), 0) as total_expenses,
        COALESCE(AVG(combined.exp), 0) as avg_expense
    FROM (
        -- 1. Invoices (Revenue)
        SELECT i.branch_id, YEAR(p.paid_at) as r_year, QUARTER(p.paid_at) as r_quarter,
               DATE_FORMAT(p.paid_at, '%Y-%m-01') as r_month, p.amount as rev, NULL as exp
        FROM payments as p
        JOIN invoices as i on p.invoice_id = i.id
        WHERE p.status IN ('PARTIAL', 'PAID')

        UNION ALL

        -- 2. Consolidate All Expenses
        SELECT branch_id, YEAR(paid_at), QUARTER(paid_at), DATE_FORMAT(paid_at, '%Y-%m-01'), NULL, amount FROM asset_maintenance_expenses
        UNION ALL
        SELECT branch_id, YEAR(paid_at), QUARTER(paid_at), DATE_FORMAT(paid_at, '%Y-%m-01'), NULL, amount FROM salary_expenses
        UNION ALL
        SELECT branch_id, YEAR(paid_at), QUARTER(paid_at), DATE_FORMAT(paid_at, '%Y-%m-01'), NULL, amount FROM utility_expenses
        UNION ALL
        SELECT branch_id, YEAR(paid_at), QUARTER(paid_at), DATE_FORMAT(paid_at, '%Y-%m-01'), NULL, amount FROM other_expenses
        UNION ALL
        SELECT branch_id, YEAR(paid_at), QUARTER(paid_at), DATE_FORMAT(paid_at, '%Y-%m-01'), NULL, amount FROM asset_expenses
        UNION ALL
        SELECT branch_id, YEAR(paid_at), QUARTER(paid_at), DATE_FORMAT(paid_at, '%Y-%m-01'), NULL, amount FROM supplies_expenses
    ) AS combined
    GROUP BY combined.branch_id, combined.r_year, combined.r_quarter, combined.r_month;

    -- Update Metadata
    INSERT INTO analytics_metadata (report_name, last_refreshed_at)
    VALUES ('FINANCIAL_SUMMARY', current_refresh_time)
    ON DUPLICATE KEY UPDATE last_refreshed_at = current_refresh_time;

    COMMIT;
END //
DELIMITER ;

COMMIT;