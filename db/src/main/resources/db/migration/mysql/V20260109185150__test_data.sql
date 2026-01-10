START TRANSACTION;

INSERT INTO `actors` (`id`, `type`, `status`, `created_at`, `deactivated_at`) VALUES
(0x16868948705e40119f42bcc324c01ce0, 'USER', 'DELETED', '2026-01-09 11:56:35.604703', '2026-01-09 12:07:52.147328'),
(0x4382e175404d4bd797f0b379bd95f64a, 'USER', 'ACTIVE', '2026-01-09 10:52:29.983531', NULL),
(0x4489e6a55bc74126884233613e7bcfe0, 'EMPLOYEE', 'ACTIVE', '2026-01-09 12:09:37.515031', NULL),
(0x57d23f23a6284bb38d28969e24f6ca50, 'EMPLOYEE', 'ACTIVE', '2026-01-09 10:54:52.260342', NULL),
(0x5bd6a420e3ab43eb80a8a4d993fc331b, 'USER', 'ACTIVE', '2026-01-09 10:52:29.297334', NULL),
(0x75a6a919bfcb427c9ede78e2f49c960d, 'USER', 'ACTIVE', '2026-01-09 10:52:26.936877', NULL),
(0x7b8bed31a74a4ea5ade4a60b42170062, 'USER', 'ACTIVE', '2026-01-09 11:36:02.568994', NULL),
(0x8b31746166d1489085a665ae7c6a92d9, 'EMPLOYEE', 'ACTIVE', '2026-01-09 10:53:12.634264', NULL),
(0xca5e103c8fbd4b9bb9510450195bc03b, 'EMPLOYEE', 'DELETED', '2026-01-09 12:13:34.940285', '2026-01-09 12:15:21.729727'),
(0xe4ee03de2a3e4a3889ee810ae14831a8, 'EMPLOYEE', 'ACTIVE', '2026-01-09 10:54:53.141638', NULL),
(0xe79d09242c4c49ddb2dfbbfad0fae629, 'EMPLOYEE', 'ACTIVE', '2026-01-09 10:54:51.687103', NULL),
(0x8140f50da33f4569b76a20c348b77222, 'MEMBER', 'ACTIVE', '2026-01-09 10:54:51.687103', NULL),
(0xcd950aff5baa4eeab5de2cd1365e5657, 'MEMBER', 'ACTIVE', '2026-01-09 10:54:51.687103', NULL),
(0xd594bb1121f44511a3fb184b9b557410, 'MEMBER', 'ACTIVE', '2026-01-09 10:54:51.687103', NULL);

INSERT INTO `users` (`id`, `actor_id`, `email`, `password`, `created_at`, `updated_at`) VALUES
(0x019ba262dd9770328a8d7e74a3af3d78, 0x75a6a919bfcb427c9ede78e2f49c960d, 'Bonnie32', 'user123', '2026-01-09 10:52:26.956371', '2026-01-09 10:52:26.956371'),
(0x019ba262e6f074b7957b10a0fd54d579, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 'Shakira_Jaskolski51', 'user123', '2026-01-09 10:52:29.302007', '2026-01-09 10:52:29.302537'),
(0x019ba262e99e77e0a3c4fb65c5850d51, 0x4382e175404d4bd797f0b379bd95f64a, 'Sofia_Hahn', 'user123', '2026-01-09 10:52:29.985095', '2026-01-09 10:52:29.985095'),
(0x019ba28ac6e5727a88fd1b640603d4e6, 0x7b8bed31a74a4ea5ade4a60b42170062, 'Michaela53', 'user123', '2026-01-09 11:36:02.590989', '2026-01-09 11:36:02.590989');

INSERT INTO `employees` (`id`, `user_id`, `actor_id`, `surname`, `first_name`, `middle_name`, `suffix`, `contact_no`, `status`, `profile_picture`, `created_at`, `updated_at`) VALUES
(0x019ba26390377eeea454fecc6baf5855, 0x019ba262dd9770328a8d7e74a3af3d78, 0x8b31746166d1489085a665ae7c6a92d9, 'Legros', 'Sophie', NULL, NULL, '09154520999', 'IN', NULL, '2026-01-09 10:53:12.637357', '2026-01-09 10:53:12.637357'),
(0x019ba2a985e97fed858ce921c11547ad, NULL, 0x4489e6a55bc74126884233613e7bcfe0, 'Lockman', 'Jasper', NULL, NULL, '09154510999', 'IN', NULL, '2026-01-09 12:09:37.517662', '2026-01-09 12:10:16.334107');

INSERT INTO `members` (`id`, `actor_id`, `surname`, `first_name`, `middle_name`, `suffix`, `status`, `profile_picture`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba2c9e74574b98e0519c19ba1fcde, 0x8140f50da33f4569b76a20c348b77222, 'Dare', 'Terrence', NULL, NULL, 'IN', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:44:59.632457', '2026-01-09 12:44:59.632457'),
(0x019ba2c9eb9f720e9987a3bfc0780a93, 0xcd950aff5baa4eeab5de2cd1365e5657, 'Schultz', 'Katarina', NULL, NULL, 'IN', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:45:00.707411', '2026-01-09 12:45:00.707940'),
(0x019ba2c9ef0f7c2f848345ca1d5a42f5, 0xd594bb1121f44511a3fb184b9b557410, 'Connelly', 'Odessa', NULL, NULL, 'IN', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:45:01.587466', '2026-01-09 12:45:01.587466');

INSERT INTO `roles` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba2676db67541a1714f0524691e73, 'ADMIN', 'Full Access', 0x8b31746166d1489085a665ae7c6a92d9, 0x8b31746166d1489085a665ae7c6a92d9, '2026-01-09 10:57:25.942328', '2026-01-09 10:57:25.942328');

INSERT INTO `permissions` (`id`, `name`) VALUES
(0x019ba2c04a027259a6a80fd67e3babbf, 'member_create'),
(0x019ba2c04a027259a6a80fd6e82d35a6, 'member_delete'),
(0x019ba2c04a0179cf901764ca8478a14e, 'member_read'),
(0x019ba2c04a027259a6a80fd7e28a01f1, 'member_update'),
(0x019ba3ba5e897479850a65be7bf02846, 'branch_create'),
(0x019ba3ba5e88747b836acfaf68b063cd, 'branch_delete'),
(0x019ba3ba5e897479850a65be0f2a082c, 'branch_read'),
(0x019ba3ba5e88747b836acfaafc1c6857, 'branch_update'),
(0x019ba3ba5e87747ebe3d630e5f3bb54a, 'branchPersonnel_create'),
(0x019ba3ba5e87747ebe3d6310ab9d34ac, 'branchPersonnel_delete'),
(0x019ba3ba5e88747b836acfaa7633d68c, 'branchPersonnel_read'),
(0x019ba3ba5e87747ebe3d631170d2aa2c, 'branchPersonnel_update'),
(0x019ba3ba5e897479850a65bfed2e0453, 'employee_create'),
(0x019ba3ba5e87747ebe3d630f9c04b5f2, 'employee_delete'),
(0x019ba3ba5e88747b836acfacb4481e30, 'employee_read'),
(0x019ba3ba5e87747ebe3d630ff80d8021, 'employee_update'),
(0x019ba3ba5e87747ebe3d630ec3f6d518, 'permission_create'),
(0x019ba3ba5e897479850a65be77b00a39, 'permission_delete'),
(0x019ba3ba5e88747b836acfabdf25808c, 'permission_read'),
(0x019ba3ba5e897479850a65c05980003a, 'permission_update'),
(0x019ba3ba5e88747b836acfad3358623a, 'role_create'),
(0x019ba3ba5e897479850a65bd3e46013e, 'role_delete'),
(0x019ba3ba5e897479850a65bf0827d3b9, 'role_read'),
(0x019ba3ba5e88747b836acfae829189d2, 'role_update'),
(0x019ba3ba5e88747b836acfafa53be9ef, 'rolePermission_delete'),
(0x019ba3ba5e88747b836acfadee8a938f, 'rolePermission_update'),
(0x019ba3ba5e897479850a65bc67eedbac, 'user_create'),
(0x019ba3ba5e757aa1afdbdcb4efe4654d, 'user_delete'),
(0x019ba3ba5e897479850a65bd7a7ce73a, 'user_read'),
(0x019ba3ba5e87747ebe3d630d6635fa0e, 'user_update');

INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES
(0x019ba2676db67541a1714f0524691e73, 0x019ba2c04a027259a6a80fd67e3babbf), -- member_create
(0x019ba2676db67541a1714f0524691e73, 0x019ba2c04a027259a6a80fd6e82d35a6), -- member_delete
(0x019ba2676db67541a1714f0524691e73, 0x019ba2c04a0179cf901764ca8478a14e), -- member_read
(0x019ba2676db67541a1714f0524691e73, 0x019ba2c04a027259a6a80fd7e28a01f1), -- member_update
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e897479850a65be7bf02846), -- branch_create
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e88747b836acfaf68b063cd), -- branch_delete
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e897479850a65be0f2a082c), -- branch_read
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e88747b836acfaafc1c6857), -- branch_update
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e87747ebe3d630e5f3bb54a), -- branchPersonnel_create
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e87747ebe3d6310ab9d34ac), -- branchPersonnel_delete
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e88747b836acfaa7633d68c), -- branchPersonnel_read
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e87747ebe3d631170d2aa2c), -- branchPersonnel_update
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e897479850a65bfed2e0453), -- employee_create
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e87747ebe3d630f9c04b5f2), -- employee_delete
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e88747b836acfacb4481e30), -- employee_read
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e87747ebe3d630ff80d8021), -- employee_update
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e87747ebe3d630ec3f6d518), -- permission_create
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e897479850a65be77b00a39), -- permission_delete
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e88747b836acfabdf25808c), -- permission_read
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e897479850a65c05980003a), -- permission_update
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e88747b836acfad3358623a), -- role_create
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e897479850a65bd3e46013e), -- role_delete
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e897479850a65bf0827d3b9), -- role_read
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e88747b836acfae829189d2), -- role_update
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e88747b836acfafa53be9ef), -- rolePermission_delete
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e88747b836acfadee8a938f), -- rolePermission_update
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e897479850a65bc67eedbac), -- user_create
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e757aa1afdbdcb4efe4654d), -- user_delete
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e897479850a65bd7a7ce73a), -- user_read
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e87747ebe3d630d6635fa0e); -- user_update

INSERT INTO `branch` (`id`, `name`, `address`, `longitude`, `latitude`, `status`, `profile_picture`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba279a6e67271893cffab220040a2, 'Matina', 'Bangkal', '125.55602001850568', '7.060337505872085', 'IN', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 11:17:20.268658', '2026-01-09 11:17:20.268658'),
(0x019ba297802d78e4804acbed3a77e6ce, 'Matinaa', 'Shrine', '125.57942199490718', '7.066796997635181', 'IN', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 11:49:56.427355', '2026-01-09 11:49:56.427355'),
(0x019ba2b747f57a9e862da61d2fc30ee6, 'Matinaaa', 'Lanang', '125.62899620875633', '7.099976731994823', 'IN', NULL, 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-01-09 12:24:39.197351', '2026-01-09 12:24:39.197351');

INSERT INTO `branch_personnel` (`id`, `actor_id`, `branch_id`, `status`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba2b8df68779199d6d4181d6c2950, 0x8b31746166d1489085a665ae7c6a92d9, 0x019ba279a6e67271893cffab220040a2, 'IN', 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:26:23.464473', '2026-01-09 12:28:13.213085'),
(0x019ba2bcf8cc74199f9d50c3e2ee9c3d, 0x4489e6a55bc74126884233613e7bcfe0, 0x019ba279a6e67271893cffab220040a2, 'IN', 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:30:52.108775', '2026-01-09 12:30:52.108775');
COMMIT;
