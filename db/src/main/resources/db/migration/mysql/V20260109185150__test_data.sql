START TRANSACTION;

INSERT INTO `actors` (`id`, `type`, `status`, `created_at`, `deactivated_at`) VALUES
(0x019484b9913a795694628f1d8c1c4912, 'SYSTEM', 'ACTIVE', '2026-01-21 11:32:11.125224', NULL),
(0x16868948705e40119f42bcc324c01ce0, 'USER', 'DELETED', '2026-01-09 11:56:35.604703', '2026-01-09 12:07:52.147328'),
(0x4382e175404d4bd797f0b379bd95f64a, 'USER', 'ACTIVE', '2026-01-09 10:52:29.983531', NULL),
(0x4489e6a55bc74126884233613e7bcfe0, 'EMPLOYEE', 'ACTIVE', '2026-01-09 12:09:37.515031', NULL),
(0x57d23f23a6284bb38d28969e24f6ca50, 'EMPLOYEE', 'ACTIVE', '2026-01-09 10:54:52.260342', NULL),
(0x5bd6a420e3ab43eb80a8a4d993fc331b, 'USER', 'ACTIVE', '2026-01-09 10:52:29.297334', NULL),
(0x71334a2138eb424983024eac34f17568, 'EMPLOYEE', 'ACTIVE', '2026-02-02 01:31:47.172538', NULL),
(0x75a6a919bfcb427c9ede78e2f49c960d, 'USER', 'ACTIVE', '2026-01-09 10:52:26.936877', NULL),
(0x7b8bed31a74a4ea5ade4a60b42170062, 'USER', 'ACTIVE', '2026-01-09 11:36:02.568994', NULL),
(0x8140f50da33f4569b76a20c348b77222, 'MEMBER', 'ACTIVE', '2026-01-09 10:54:51.687103', NULL),
(0x8b31746166d1489085a665ae7c6a92d9, 'EMPLOYEE', 'ACTIVE', '2026-01-09 10:53:12.634264', NULL),
(0xca5e103c8fbd4b9bb9510450195bc03b, 'EMPLOYEE', 'DELETED', '2026-01-09 12:13:34.940285', '2026-01-09 12:15:21.729727'),
(0xcd950aff5baa4eeab5de2cd1365e5657, 'MEMBER', 'ACTIVE', '2026-01-09 10:54:51.687103', NULL),
(0xd594bb1121f44511a3fb184b9b557410, 'MEMBER', 'ACTIVE', '2026-01-09 10:54:51.687103', NULL),
(0xe4ee03de2a3e4a3889ee810ae14831a8, 'EMPLOYEE', 'ACTIVE', '2026-01-09 10:54:53.141638', NULL),
(0xe79d09242c4c49ddb2dfbbfad0fae629, 'EMPLOYEE', 'ACTIVE', '2026-01-09 10:54:51.687103', NULL),
(0xf520a8fb382443398bb43732c8a3f617, 'USER', 'ACTIVE', '2026-01-09 11:37:31.568994', NULL);

INSERT INTO `users` (`id`, `actor_id`, `email`, `password`, `created_at`, `updated_at`) VALUES
(0x019ba262dd9770328a8d7e74a3af3d78, 0x75a6a919bfcb427c9ede78e2f49c960d, 'Bonnie32', '$argon2id$v=19$m=16384,t=2,p=1$KB7xzE43pNh5tKU21ms1Sw$neyzU8pin1RjbA2cTXFOQXmz5p8CPQ3QBE/BE/qq1a4', '2026-01-09 10:52:26.956371', '2026-01-09 10:52:26.956371'),
(0x019ba262e6f074b7957b10a0fd54d579, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 'Shakira_Jaskolski51', '$argon2id$v=19$m=16384,t=2,p=1$ax+ArfDUeFk6dUJSyqd0qg$CH2RGzi4OrAhgDdNFKqwmYRHQM8q9NPJvQZX7Jz48qw', '2026-01-09 10:52:29.302007', '2026-01-09 10:52:29.302537'),
(0x019ba262e99e77e0a3c4fb65c5850d51, 0x4382e175404d4bd797f0b379bd95f64a, 'Sofia_Hahn', '$argon2id$v=19$m=16384,t=2,p=1$TFpmY6PXLslhJrFcL7kQaw$kykWfF1cXD0/4xcGbglq9rlfmoG7g/1qFAN1MFuJIg0', '2026-01-09 10:52:29.985095', '2026-01-09 10:52:29.985095'),
(0x019bafe2f4ee72e7b1271d6b9d09b8d7, 0xf520a8fb382443398bb43732c8a3f617, 'admin', '$argon2id$v=19$m=16384,t=2,p=1$lZjfRpq86VBpBX+Xy+7ctg$WRvQvkIVfzSZZm+HWgyUEXRoqdtdnwpYM+/XPhDmGiQ', '2026-01-12 01:47:25.411764', '2026-01-12 01:47:25.411764'),
(0x019ba28ac6e5727a88fd1b640603d4e6, 0x7b8bed31a74a4ea5ade4a60b42170062, 'Michaela53', '$argon2id$v=19$m=16384,t=2,p=1$JdBo+giX4CINdz+JijRJGQ$WuWZmLKzODyj2x8XgbTWL+ICq+UIo0A7imA5eKerKvk', '2026-01-09 11:36:02.590989', '2026-01-09 11:36:02.590989');

INSERT INTO `employees` (`id`, `user_id`, `actor_id`, `surname`, `first_name`, `middle_name`, `suffix`, `contact_no`, `status`, `profile_picture`, `created_at`, `updated_at`) VALUES
(0x019ba26390377eeea454fecc6baf5855, 0x019ba262dd9770328a8d7e74a3af3d78, 0x8b31746166d1489085a665ae7c6a92d9, 'Legros', 'Sophie', NULL, NULL, '09154520999', 'IN', NULL, '2026-01-09 10:53:12.637357', '2026-01-09 10:53:12.637357'),
(0x019ba2a985e97fed858ce921c11547ad, NULL, 0x4489e6a55bc74126884233613e7bcfe0, 'Lockman', 'Jasper', NULL, NULL, '09154510999', 'IN', NULL, '2026-01-09 12:09:37.517662', '2026-01-09 12:10:16.334107'),
(0x019c1bfa305b7bf5a2d7d880b0351e08, 0x019bafe2f4ee72e7b1271d6b9d09b8d7, 0x71334a2138eb424983024eac34f17568, 'Sipes', 'Emile', NULL, 'A', '09154520999', 'IN', NULL, '2026-02-02 01:31:47.188027', '2026-02-02 01:31:47.188027');

INSERT INTO `members` (`id`, `actor_id`, `surname`, `first_name`, `middle_name`, `suffix`, `status`, `profile_picture`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba2c9e74574b98e0519c19ba1fcde, 0x8140f50da33f4569b76a20c348b77222, 'Dare', 'Terrence', NULL, NULL, 'ACTIVE', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:44:59.632457', '2026-01-09 12:44:59.632457'),
(0x019ba2c9eb9f720e9987a3bfc0780a93, 0xcd950aff5baa4eeab5de2cd1365e5657, 'Schultz', 'Katarina', NULL, NULL, 'ACTIVE', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:45:00.707411', '2026-01-09 12:45:00.707940'),
(0x019ba2c9ef0f7c2f848345ca1d5a42f5, 0xd594bb1121f44511a3fb184b9b557410, 'Connelly', 'Odessa', NULL, NULL, 'ACTIVE', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:45:01.587466', '2026-01-09 12:45:01.587466');

INSERT INTO `roles` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba2676db67541a1714f0524691e73, 'ADMIN', 'Full Access', 0x8b31746166d1489085a665ae7c6a92d9, 0x8b31746166d1489085a665ae7c6a92d9, '2026-01-09 10:57:25.942328', '2026-01-09 10:57:25.942328'),
(0x019c52decb087221bbf328a31a517376, 'Test', 'No Access', 0x8b31746166d1489085a665ae7c6a92d9, 0x8b31746166d1489085a665ae7c6a92d9, '2026-01-09 10:57:25.942328', '2026-01-09 10:57:25.942328');

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(0x019bafe2f4ee72e7b1271d6b9d09b8d7, 0x019ba2676db67541a1714f0524691e73);

INSERT INTO `permissions` (`id`, `name`) VALUES
(0x019bd58eff7473009e875069c52f54be, 'asset_create'),
(0x019bd58eff637c2b87b654e7a15ea030, 'asset_delete'),
(0x019bd58eff7473009e87506608bff869, 'asset_read'),
(0x019bd58eff7473009e875066f42d80fd, 'asset_update'),
(0x019c51285db9751685c0f8be9b1dd94b, 'assetExpense_create'),
(0x019c51285db9751685c0f8bfa0a4c5dd, 'assetExpense_delete'),
(0x019c51285db9751685c0f8bf1e96aef9, 'assetExpense_read'),
(0x019c51285db87cafa229772bd81f307e, 'assetExpense_update'),
(0x019bd58eff7473009e875067d60e0bbb, 'assetCategory_create'),
(0x019bd58eff7473009e875068df8c0ad0, 'assetCategory_delete'),
(0x019bd58eff7473009e875067619b5653, 'assetCategory_read'),
(0x019bd58eff7473009e875068320448af, 'assetCategory_update'),
(0x019be4458174715e9ad763f4040e7013, 'assetMaintenance_read'),
(0x019be479605678fbb10f3f64f56ae528, 'assetMaintenance_update'),
(0x019c5128a4fe7838ab3f9f35022bdf7a, 'assetMaintenanceExpense_create'),
(0x019c5128a4fe7838ab3f9f34ab7b6a2c, 'assetMaintenanceExpense_delete'),
(0x019c5128a4fe7838ab3f9f34879c650d, 'assetMaintenanceExpense_read'),
(0x019c5128a4fe7838ab3f9f35e3d5281c, 'assetMaintenanceExpense_update'),
(0x019bdedaf07975009778b92857da58b4, 'attendance_create'),
(0x019bdedaf07975009778b9293fdfc819, 'attendance_delete'),
(0x019bdedaf0787c83832caeaa986c720d, 'attendance_read'),
(0x019bdedaf07975009778b927276aed54, 'attendance_update'),
(0x019bdedaf07975009778b928222f3c0f, 'attendanceType_create'),
(0x019bdedaf0787c83832caeaac7c42a46, 'attendanceType_delete'),
(0x019bdedaf0787c83832caeab8a8299dc, 'attendanceType_read'),
(0x019bdedaf07975009778b927f1fc399a, 'attendanceType_update'),
(0x019bb559885f7175ba1eabb10ef4290f, 'billingCycle_create'),
(0x019bb559885e7175a417424ac0ae00f8, 'billingCycle_delete'),
(0x019bb559885f7175ba1eabb10d35fbf3, 'billingCycle_read'),
(0x019bb559885e7175a417424d1386f4d4, 'billingCycle_update'),
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
(0x019bded9eaf77bf2a490bbc1b6327a8d, 'invoice_create'),
(0x019bded9eaf870739e96d9f9125cd9ee, 'invoice_delete'),
(0x019bded9eaf870739e96d9f81fb0543a, 'invoice_read'),
(0x019bded9eaf57bbc83180396611b953e, 'invoice_update'),
(0x019bded9eaf67bbda6fad79ae9f87125, 'ledger_create'),
(0x019bded9eaf67bbda6fad79b18d2bbb5, 'ledger_delete'),
(0x019bded9eaf67bbda6fad79b5ea8178c, 'ledger_read'),
(0x019bded9eaf67bbda6fad79b178b51c6, 'ledger_update'),
(0x019bdf23e77d7bd786924eda392f2ee0, 'maintenanceSchedule_create'),
(0x019bdf23e77d7bd786924edaa8eba0cc, 'maintenanceSchedule_delete'),
(0x019bdf23e77479c7a970d2b75fcb3092, 'maintenanceSchedule_read'),
(0x019bdf23e77d7bd786924edaa62a87ab, 'maintenanceSchedule_update'),
(0x019ba2c04a027259a6a80fd67e3babbf, 'member_create'),
(0x019ba2c04a027259a6a80fd6e82d35a6, 'member_delete'),
(0x019ba2c04a0179cf901764ca8478a14e, 'member_read'),
(0x019ba2c04a027259a6a80fd7e28a01f1, 'member_update'),
(0x019bdedaa168715c81b1bbe34db38ec2, 'memberProgress_create'),
(0x019bdedaa168715c81b1bbe2c13637cb, 'memberProgress_delete'),
(0x019bdedaa16879e5bdcdebfb7d037450, 'memberProgress_read'),
(0x019bdedaa16972889d6c55db67e98ca1, 'memberProgress_update'),
(0x019bbcda0fe373488f9675fac0fa30ca, 'memberSubscription_create'),
(0x019bbcda0fe373488f9675faf01731f4, 'memberSubscription_delete'),
(0x019bbcda0fda771e87dcc1b0bfa69f06, 'memberSubscription_read'),
(0x019bbcda0fe373488f9675f9d50d9d60, 'memberSubscription_update'),
(0x019bd458aafa7f34bf419ca0dfa1bddd, 'objectStorage_delete'),
(0x019bd460624e79238c67993a76d64e78, 'objectStorage_read'),
(0x019bd458aaf0749e8723c6e9ace4c91c, 'objectStorage_update'),
(0x019bd458aafa7f34bf419ca0a74ebb71, 'objectStorage_create'),
(0x019c51293e1f75b1a64eecaa4f91bbb4, 'otherExpense_create'),
(0x019c51293e1f75b1a64eecaaf4214ac2, 'otherExpense_delete'),
(0x019c51293e1e7d6f94de1b35401dc7fe, 'otherExpense_read'),
(0x019c51293e1e7d6f94de1b35c83d5225, 'otherExpense_update'),
(0x019bded9eaec78ef9f56a9072ea88ce2, 'payment_create'),
(0x019bded9eaf57bbc831803963ba6537d, 'payment_delete'),
(0x019bded9eaf870739e96d9f9f5108f78, 'payment_read'),
(0x019bded9eaf77bf2a490bbc2a6f3f0ed, 'payment_update'),
(0x019bdfbc47de7a11924a32491a16332d, 'paymentMethod_create'),
(0x019bdfbc48037322b63a40f6f3cddcd5, 'paymentMethod_delete'),
(0x019bdfbc48037322b63a40f72fcc68c7, 'paymentMethod_read'),
(0x019bdfbc48037322b63a40f7f128dbd9, 'paymentMethod_update'),
(0x019ba3ba5e87747ebe3d630ec3f6d518, 'permission_create'),
(0x019ba3ba5e897479850a65be77b00a39, 'permission_delete'),
(0x019ba3ba5e88747b836acfabdf25808c, 'permission_read'),
(0x019ba3ba5e897479850a65c05980003a, 'permission_update'),
(0x019c7529f2d8744a99ea6c7af76a4564, 'personnelRole_create'),
(0x019c7529f2df74caa54615ee3e2224f9, 'personnelRole_delete'),
(0x019c7529f2df74caa54615eeedabf717, 'personnelRole_read'),
(0x019c7529f2df74caa54615ed5b9ff2b8, 'personnelRole_update'),
(0x019bdedaa16972889d6c55d9e6f09595, 'progress_create'),
(0x019bdedaa16879e5bdcdebf93342c00c, 'progress_delete'),
(0x019bdedaa16879e5bdcdebfaf6b299c5, 'progress_read'),
(0x019bdedaa16972889d6c55daa57c37d2, 'progress_update'),
(0x019bdedaa168715c81b1bbe2e018c43b, 'progressOption_create'),
(0x019bdedaa16879e5bdcdebfa0c113edf, 'progressOption_delete'),
(0x019bdedaa16778f1b5be9d03f8c576cb, 'progressOption_read'),
(0x019bdedaa16972889d6c55db6ab375db, 'progressOption_update'),
(0x019c40fe24427ef480fe55107665e327, 'report_create'),
(0x019c40fe24427ef480fe5510f6b948bb, 'report_delete'),
(0x019c40fe24427ef480fe5511533a266c, 'report_read'),
(0x019c40fe24427ef480fe550fed7b79c2, 'report_update'),
(0x019c40fe24427ef480fe5511585a5852, 'reportType_create'),
(0x019c40fe24427ef480fe5512542904e9, 'reportType_delete'),
(0x019c40fe24427ef480fe55103544f417, 'reportType_read'),
(0x019c40fe24427657a6e179fd612b3155, 'reportType_update'),
(0x019ba3ba5e88747b836acfad3358623a, 'role_create'),
(0x019ba3ba5e897479850a65bd3e46013e, 'role_delete'),
(0x019ba3ba5e897479850a65bf0827d3b9, 'role_read'),
(0x019ba3ba5e88747b836acfae829189d2, 'role_update'),
(0x019ba3ba5e88747b836acfafa53be9ef, 'rolePermission_delete'),
(0x019ba3ba5e88747b836acfadee8a938f, 'rolePermission_update'),
(0x019c512961877ee89b65da88a686af12, 'salaryExpense_create'),
(0x019c512961877ee89b65da88d12785ef, 'salaryExpense_delete'),
(0x019c512961877ee89b65da880e997023, 'salaryExpense_read'),
(0x019c512961877ee89b65da8827003bae, 'salaryExpense_update'),
(0x019bb559885f7175ba1eabb0b997e030, 'subscription_create'),
(0x019bb559885e7175a417424acca5fcb2, 'subscription_delete'),
(0x019bb559885f7175ba1eabb1938f33b6, 'subscription_read'),
(0x019bb559885f7175ba1eabb254390e8f, 'subscription_update'),
(0x019bb5598846778a88d5a3c3a11ad6a9, 'subscriptionAvailed_create'),
(0x019bb559885e7175a417424c8bacb0e3, 'subscriptionAvailed_delete'),
(0x019bb559885e7175a417424a8d6b5f97, 'subscriptionAvailed_read'),
(0x019bb559885e7175a417424ba1dc0b6e, 'subscriptionAvailed_update'),
(0x019c512ac78a72ffb9e50eff79cd8e93, 'suppliesExpense_create'),
(0x019c512ac78a7b858ffbb340b9c0cdf5, 'suppliesExpense_delete'),
(0x019c512ac78a72ffb9e50efe8871442e, 'suppliesExpense_read'),
(0x019c512ac78a7b858ffbb34135e5aa78, 'suppliesExpense_update'),
(0x019c1cf48f427abca8e9cf32cc19c15f, 'suppliesLog_create'),
(0x019c1cf48f427abca8e9cf33ff8ac7e4, 'suppliesLog_delete'),
(0x019c1cf48f437abcb2fe04b4619fca4a, 'suppliesLog_read'),
(0x019c1cf48f437abcb2fe04b4d924df25, 'suppliesLog_update'),
(0x019c1cf48f427abca8e9cf33c6a0b80d, 'supply_create'),
(0x019c1cf48f427abca8e9cf32f94ffcde, 'supply_delete'),
(0x019c1cf48f437abcb2fe04b3bb9a6619, 'supply_read'),
(0x019c1cf48f3a74f3b1407976bc709fbc, 'supply_update'),
(0x019ba3ba5e897479850a65bc67eedbac, 'user_create'),
(0x019ba3ba5e757aa1afdbdcb4efe4654d, 'user_delete'),
(0x019ba3ba5e897479850a65bd7a7ce73a, 'user_read'),
(0x019ba3ba5e87747ebe3d630d6635fa0e, 'user_update'),
(0x019c51290894791e80e8874d9904c851, 'utilityExpense_create'),
(0x019c51290894791e80e8874e02879afc, 'utilityExpense_delete'),
(0x019c51290894791e80e8874c4acf781e, 'utilityExpense_read'),
(0x019c51290894791e80e8874cee3625b6, 'utilityExpense_update');

INSERT INTO `role_permissions` (`role_id`, `permission_id`) VALUES
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaf07975009778b92857da58b4), -- attendance_create
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaf07975009778b9293fdfc819), -- attendance_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaf0787c83832caeaa986c720d), -- attendance_read
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaf07975009778b927276aed54), -- attendance_update
(0x019ba2676db67541a1714f0524691e73, 0x019c7529f2d8744a99ea6c7af76a4564), -- personnelRole_create
(0x019ba2676db67541a1714f0524691e73, 0x019c7529f2df74caa54615ee3e2224f9), -- personnelRole_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c7529f2df74caa54615eeedabf717), -- personnelRole_read
(0x019ba2676db67541a1714f0524691e73, 0x019c7529f2df74caa54615ed5b9ff2b8), -- personnelRole_update
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf77bf2a490bbc1b6327a8d), -- invoice_create
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf870739e96d9f9125cd9ee), -- invoice_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf870739e96d9f81fb0543a), -- invoice_read
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf57bbc83180396611b953e), -- invoice_update
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf67bbda6fad79ae9f87125), -- ledger_create
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf67bbda6fad79b18d2bbb5), -- ledger_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf67bbda6fad79b5ea8178c), -- ledger_read
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf67bbda6fad79b178b51c6), -- ledger_update
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa168715c81b1bbe34db38ec2), -- memberProgress_create
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa168715c81b1bbe2c13637cb), -- memberProgress_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa16879e5bdcdebfb7d037450), -- memberProgress_read
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa16972889d6c55db67e98ca1), -- memberProgress_update
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaec78ef9f56a9072ea88ce2), -- payment_create
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf57bbc831803963ba6537d), -- payment_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf870739e96d9f9f5108f78), -- payment_read
(0x019ba2676db67541a1714f0524691e73, 0x019bded9eaf77bf2a490bbc2a6f3f0ed), -- payment_update
(0x019ba2676db67541a1714f0524691e73, 0x019bdfbc47de7a11924a32491a16332d), -- paymentMethod_create
(0x019ba2676db67541a1714f0524691e73, 0x019bdfbc48037322b63a40f6f3cddcd5), -- paymentMethod_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bdfbc48037322b63a40f72fcc68c7), -- paymentMethod_read
(0x019ba2676db67541a1714f0524691e73, 0x019bdfbc48037322b63a40f7f128dbd9), -- paymentMethod_update
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa16972889d6c55d9e6f09595), -- progress_create
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa16879e5bdcdebf93342c00c), -- progress_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa16879e5bdcdebfaf6b299c5), -- progress_read
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa16972889d6c55daa57c37d2), -- progress_update
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa168715c81b1bbe2e018c43b), -- progressOption_create
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa16879e5bdcdebfa0c113edf), -- progressOption_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa16778f1b5be9d03f8c576cb), -- progressOption_read
(0x019ba2676db67541a1714f0524691e73, 0x019bdedaa16972889d6c55db6ab375db), -- progressOption_update
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885f7175ba1eabb10ef4290f), -- billingCycle_create
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885e7175a417424ac0ae00f8), -- billingCycle_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885f7175ba1eabb10d35fbf3), -- billingCycle_read
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885e7175a417424d1386f4d4), -- billingCycle_update
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885f7175ba1eabb0b997e030), -- subscription_create
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885e7175a417424acca5fcb2), -- subscription_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885f7175ba1eabb1938f33b6), -- subscription_read
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885f7175ba1eabb254390e8f), -- subscription_update
(0x019ba2676db67541a1714f0524691e73, 0x019bb5598846778a88d5a3c3a11ad6a9), -- subscriptionAvailed_create
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885e7175a417424c8bacb0e3), -- subscriptionAvailed_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885e7175a417424a8d6b5f97), -- subscriptionAvailed_read
(0x019ba2676db67541a1714f0524691e73, 0x019bb559885e7175a417424ba1dc0b6e), -- subscriptionAvailed_update
(0x019ba2676db67541a1714f0524691e73, 0x019bbcda0fe373488f9675fac0fa30ca), -- memberSubscription_create
(0x019ba2676db67541a1714f0524691e73, 0x019bbcda0fe373488f9675faf01731f4), -- memberSubscription_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bbcda0fda771e87dcc1b0bfa69f06), -- memberSubscription_read
(0x019ba2676db67541a1714f0524691e73, 0x019bbcda0fe373488f9675f9d50d9d60), -- memberSubscription_update
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
(0x019ba2676db67541a1714f0524691e73, 0x019bd460624e79238c67993a76d64e78), -- objectStorage_read
(0x019ba2676db67541a1714f0524691e73, 0x019bd458aafa7f34bf419ca0dfa1bddd), -- objectStorage_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bd458aaf0749e8723c6e9ace4c91c), -- objectStorage_update
(0x019ba2676db67541a1714f0524691e73, 0x019bd458aafa7f34bf419ca0a74ebb71), -- objectStorage_upload
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
(0x019ba2676db67541a1714f0524691e73, 0x019ba3ba5e87747ebe3d630d6635fa0e), -- user_update
(0x019ba2676db67541a1714f0524691e73, 0x019bd58eff7473009e875069c52f54be), -- asset_create
(0x019ba2676db67541a1714f0524691e73, 0x019bd58eff637c2b87b654e7a15ea030), -- asset_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bd58eff7473009e87506608bff869), -- asset_read
(0x019ba2676db67541a1714f0524691e73, 0x019bd58eff7473009e875066f42d80fd), -- asset_update
(0x019ba2676db67541a1714f0524691e73, 0x019bd58eff7473009e875067d60e0bbb), -- assetCategory_create
(0x019ba2676db67541a1714f0524691e73, 0x019bd58eff7473009e875068df8c0ad0), -- assetCategory_delete
(0x019ba2676db67541a1714f0524691e73, 0x019bd58eff7473009e875067619b5653), -- assetCategory_read
(0x019ba2676db67541a1714f0524691e73, 0x019bd58eff7473009e875068320448af), -- assetCategory_update
(0x019ba2676db67541a1714f0524691e73, 0x019c51290894791e80e8874d9904c851), -- utilityExpense_create
(0x019ba2676db67541a1714f0524691e73, 0x019c51290894791e80e8874e02879afc), -- utilityExpense_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c51290894791e80e8874c4acf781e), -- utilityExpense_read
(0x019ba2676db67541a1714f0524691e73, 0x019c51290894791e80e8874cee3625b6), -- utilityExpense_update
(0x019ba2676db67541a1714f0524691e73, 0x019c51293e1f75b1a64eecaa4f91bbb4), -- otherExpense_create
(0x019ba2676db67541a1714f0524691e73, 0x019c51293e1f75b1a64eecaaf4214ac2), -- otherExpense_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c51293e1e7d6f94de1b35401dc7fe), -- otherExpense_read
(0x019ba2676db67541a1714f0524691e73, 0x019c51293e1e7d6f94de1b35c83d5225), -- otherExpense_update
(0x019ba2676db67541a1714f0524691e73, 0x019c512961877ee89b65da88a686af12), -- salaryExpense_create
(0x019ba2676db67541a1714f0524691e73, 0x019c512961877ee89b65da88d12785ef), -- salaryExpense_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c512961877ee89b65da880e997023), -- salaryExpense_read
(0x019ba2676db67541a1714f0524691e73, 0x019c512961877ee89b65da8827003bae), -- salaryExpense_update
(0x019ba2676db67541a1714f0524691e73, 0x019c51285db9751685c0f8be9b1dd94b), -- assetExpense_create
(0x019ba2676db67541a1714f0524691e73, 0x019c51285db9751685c0f8bfa0a4c5dd), -- assetExpense_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c51285db9751685c0f8bf1e96aef9), -- assetExpense_read
(0x019ba2676db67541a1714f0524691e73, 0x019c51285db87cafa229772bd81f307e), -- assetExpense_update
(0x019ba2676db67541a1714f0524691e73, 0x019c5128a4fe7838ab3f9f35022bdf7a), -- assetMaintenanceExpense_create
(0x019ba2676db67541a1714f0524691e73, 0x019c5128a4fe7838ab3f9f34ab7b6a2c), -- assetMaintenanceExpense_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c5128a4fe7838ab3f9f34879c650d), -- assetMaintenanceExpense_read
(0x019ba2676db67541a1714f0524691e73, 0x019c5128a4fe7838ab3f9f35e3d5281c), -- assetMaintenanceExpense_update
(0x019ba2676db67541a1714f0524691e73, 0x019c512ac78a72ffb9e50eff79cd8e93), -- suppliesExpense_create
(0x019ba2676db67541a1714f0524691e73, 0x019c512ac78a7b858ffbb340b9c0cdf5), -- suppliesExpense_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c512ac78a72ffb9e50efe8871442e), -- suppliesExpense_read
(0x019ba2676db67541a1714f0524691e73, 0x019c512ac78a7b858ffbb34135e5aa78), -- suppliesExpense_update
(0x019ba2676db67541a1714f0524691e73, 0x019bdf23e77479c7a970d2b75fcb3092), -- maintenanceSchedule_read
(0x019ba2676db67541a1714f0524691e73, 0x019bdf23e77d7bd786924eda392f2ee0), -- maintenanceSchedule_create
(0x019ba2676db67541a1714f0524691e73, 0x019bdf23e77d7bd786924edaa62a87ab), -- maintenanceSchedule_update
(0x019ba2676db67541a1714f0524691e73, 0x019bdf23e77d7bd786924edaa8eba0cc), -- maintenanceSchedule_delete
(0x019ba2676db67541a1714f0524691e73, 0x019be4458174715e9ad763f4040e7013), -- assetMaintenance_read
(0x019ba2676db67541a1714f0524691e73, 0x019be479605678fbb10f3f64f56ae528), -- assetMaintenance_update
(0x019ba2676db67541a1714f0524691e73, 0x019c1cf48f427abca8e9cf32cc19c15f), -- suppliesLog_create
(0x019ba2676db67541a1714f0524691e73, 0x019c1cf48f427abca8e9cf33ff8ac7e4), -- suppliesLog_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c1cf48f437abcb2fe04b4619fca4a), -- suppliesLog_read
(0x019ba2676db67541a1714f0524691e73, 0x019c1cf48f437abcb2fe04b4d924df25), -- suppliesLog_update
(0x019ba2676db67541a1714f0524691e73, 0x019c1cf48f427abca8e9cf33c6a0b80d), -- supply_create
(0x019ba2676db67541a1714f0524691e73, 0x019c1cf48f437abcb2fe04b3bb9a6619), -- supply_read
(0x019ba2676db67541a1714f0524691e73, 0x019c1cf48f3a74f3b1407976bc709fbc), -- supply_update
(0x019ba2676db67541a1714f0524691e73, 0x019c1cf48f427abca8e9cf32f94ffcde), -- supply_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c40fe24427ef480fe550fed7b79c2), -- report_update
(0x019ba2676db67541a1714f0524691e73, 0x019c40fe24427ef480fe55107665e327), -- report_create
(0x019ba2676db67541a1714f0524691e73, 0x019c40fe24427ef480fe5510f6b948bb), -- report_delete
(0x019ba2676db67541a1714f0524691e73, 0x019c40fe24427ef480fe5511533a266c), -- report_read
(0x019ba2676db67541a1714f0524691e73, 0x019c40fe24427657a6e179fd612b3155), -- reportType_update
(0x019ba2676db67541a1714f0524691e73, 0x019c40fe24427ef480fe55103544f417), -- reportType_read
(0x019ba2676db67541a1714f0524691e73, 0x019c40fe24427ef480fe5511585a5852), -- reportType_create
(0x019ba2676db67541a1714f0524691e73, 0x019c40fe24427ef480fe5512542904e9); -- reportType_delete

INSERT INTO `branch` (`id`, `name`, `address`, `longitude`, `latitude`, `status`, `profile_picture`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba279a6e67271893cffab220040a2, 'Matina', 'Bangkal', '125.55602001850568', '7.060337505872085', 'ACTIVE', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 11:17:20.268658', '2026-01-09 11:17:20.268658'),
(0x019ba297802d78e4804acbed3a77e6ce, 'Matinaa', 'Shrine', '125.57942199490718', '7.066796997635181', 'ACTIVE', NULL, 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 11:49:56.427355', '2026-01-09 11:49:56.427355'),
(0x019ba2b747f57a9e862da61d2fc30ee6, 'Matinaaa', 'Lanang', '125.62899620875633', '7.099976731994823', 'ACTIVE', NULL, 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-01-09 12:24:39.197351', '2026-01-09 12:24:39.197351');

INSERT INTO `personnel_roles` (`id`, `name`, `description`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019eeee76db67541a1714f0524691e73, 'Manager', 'Responsible for being a manager', 0x8b31746166d1489085a665ae7c6a92d9, 0x8b31746166d1489085a665ae7c6a92d9, '2026-01-09 10:57:25.942328', '2026-01-09 10:57:25.942328'),
(0x019eeeeecb087221bbf328a31a517376, 'Test', 'No Access', 0x8b31746166d1489085a665ae7c6a92d9, 0x8b31746166d1489085a665ae7c6a92d9, '2026-01-09 10:57:25.942328', '2026-01-09 10:57:25.942328');

INSERT INTO `branch_personnel` (`id`, `actor_id`, `branch_id`, `personnel_role_id`, `status`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba2b8df68779199d6d4181d6c2950, 0x8b31746166d1489085a665ae7c6a92d9, 0x019ba279a6e67271893cffab220040a2, 0x019eeee76db67541a1714f0524691e73, 'ACTIVE', 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:26:23.464473', '2026-01-09 12:28:13.213085'),
(0x019ba2bcf8cc74199f9d50c3e2ee9c3d, 0x4489e6a55bc74126884233613e7bcfe0, 0x019ba279a6e67271893cffab220040a2, 0x019eeee76db67541a1714f0524691e73, 'ACTIVE', 0x5bd6a420e3ab43eb80a8a4d993fc331b, 0x5bd6a420e3ab43eb80a8a4d993fc331b, '2026-01-09 12:30:52.108775', '2026-01-09 12:30:52.108775'),
(0x019c1bfae7e177f5bf7fc344d7680e77, 0x71334a2138eb424983024eac34f17568, 0x019ba279a6e67271893cffab220040a2, 0x019eeee76db67541a1714f0524691e73, 'ACTIVE', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-02 01:32:34.146019', '2026-02-02 01:32:34.146019');

INSERT INTO `billing_cycles` (`id`, `name`, `intervals`, `interval_count`, `grace_period_days`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019bd4716bcc799c89953f5e7b76cabf, 'Bi-weekly', 'WEEKLY', 2, 7, 0x7b8bed31a74a4ea5ade4a60b42170062, 0x7b8bed31a74a4ea5ade4a60b42170062, '2026-01-19 04:09:21.621513', '2026-01-19 04:09:21.621513'),
(0x019bd4719cd07d638f0f6305426f29a1, 'Monthly', 'MONTHLY', 1, 7, 0x7b8bed31a74a4ea5ade4a60b42170062, 0x7b8bed31a74a4ea5ade4a60b42170062, '2026-01-19 04:09:34.161889', '2026-01-19 04:09:34.161889');

INSERT INTO `subscriptions` (`id`, `billing_cycle_id`, `name`, `description`, `amount`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019bd4733fd77ffe8f6b4b5216aecc62, 0x019bd4719cd07d638f0f6305426f29a1, 'Silver', 'Basic Monthly plan for casual members with service to most equipment', 1299.99, 0x7b8bed31a74a4ea5ade4a60b42170062, 0x7b8bed31a74a4ea5ade4a60b42170062, '2026-01-19 04:11:21.432568', '2026-01-19 04:11:21.432568'),
(0x019bd4733fd7456e8f6b4b5216afcf22, 0x019bd4719cd07d638f0f6305426f29a1, 'Test', 'Basic Monthly plan for casual members with service to most equipment', 1299.99, 0x7b8bed31a74a4ea5ade4a60b42170062, 0x7b8bed31a74a4ea5ade4a60b42170062, '2026-01-19 04:11:21.432568', '2026-01-19 04:11:21.432568');

INSERT INTO `subscriptions_availed` (`id`, `subscription_id`, `name`, `amount`, `intervals`, `interval_count`, `grace_period_days`) VALUES
(0x019bd47aea9279f584c8224f9cca1972, 0x019bd4733fd77ffe8f6b4b5216aecc62, 'Silver', 1299.99, 'MONTHLY', 1, 7),
(0x019c6fd8694b78649177ae92006e476f, 0x019bd4733fd77ffe8f6b4b5216aecc62, 'Test', 1299.99, 'MONTHLY', 1, 7);

INSERT INTO `member_subscriptions` (`id`, `actor_id`, `subscription_availed_id`, `branch_id`, `start_date`, `end_date`, `status`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019bd47aeacd7d4789afe3f9285da33a, 0x8140f50da33f4569b76a20c348b77222, 0x019bd47aea9279f584c8224f9cca1972, 0x019ba279a6e67271893cffab220040a2, '2026-01-19 04:19:44.000000', NULL, 'ACTIVE', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-01-19 04:19:43.951676', '2026-01-19 04:19:43.951676'),
(0x019bd47aeacd7d478140f50da33f4569, 0x8140f50da33f4569b76a20c348b77222, 0x019bd47aea9279f584c8224f9cca1972, 0x019ba279a6e67271893cffab220040a2, '2026-01-19 04:19:44.000000', NULL, 'ACTIVE', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-01-19 04:19:43.951676', '2026-01-19 04:19:43.951676');

INSERT INTO `invoices` (`id`, `actor_id`, `subscription_availed_id`, `branch_id`, `member_subscription_id`, `status`, `subtotal`, `discount`, `convenience_fee`, `total`, `due_date`, `grace_period_date`, `issued_at`, `system_generated`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019c716e86d47aeba8d2517c464a9eab, 0xcd950aff5baa4eeab5de2cd1365e5657, 0x019bd47aea9279f584c8224f9cca1972, 0x019ba279a6e67271893cffab220040a2, 0x019bd47aeacd7d4789afe3f9285da33a, 'PENDING', 1000.00, NULL, NULL, 1000.00, '2026-03-2 10:50:43.000000', '2026-03-2 15:46:35.000000', '2026-03-2 15:46:34.835682', 1, 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-03-2 15:46:34.837189', '2026-03-2 15:46:34.837189'),
(0x019c716e86d47aeba8d2517c7982a8d0, 0xcd950aff5baa4eeab5de2cd1365e5657, 0x019bd47aea9279f584c8224f9cca1972, 0x019ba279a6e67271893cffab220040a2, 0x019bd47aeacd7d4789afe3f9285da33a, 'PENDING', 1000.00, NULL, NULL, 1000.00, '2026-03-3 15:46:35.000000', '2026-03-3 15:46:35.000000', '2026-03-3 15:46:34.835682', 1, 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-03-3 15:46:34.837189', '2026-03-3 15:46:34.837189');

INSERT INTO `payment_methods` (`id`, `name`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019c748b4b927982a8d041ebfa856dd8, 'G-Cash', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 06:16:51.859124', '2026-02-19 06:16:51.859124'),
(0x019c748b6dd77f578135c86b077d7f77, 'Cash', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 06:17:00.632486', '2026-02-19 06:17:00.632486'),
(0x019c748b848f79ac82df9cd6fa601a45, 'Card', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 06:17:06.448128', '2026-02-19 06:17:06.448128');

INSERT INTO `payments` (`id`, `invoice_id`, `reference_num`, `status`, `payment_method_id`, `amount`, `paid_at`, `failure_reason`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019c748e57dd71cc8ad301923aa45f12, 0x019c716e86d47aeba8d2517c464a9eab, NULL, 'FULL', 0x019c748b4b927982a8d041ebfa856dd8, 1000.00, NULL, NULL, 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 06:20:11.614664', '2026-02-19 06:20:11.614664');

INSERT INTO `progress_options` (`id`, `name`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019c75eda9f87b539ff5376498e646b8, 'Workout Progression', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 12:43:55.768708', '2026-02-19 12:43:55.768708'),
(0x019c75edc467797da79b938091385e88, 'Test', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 12:44:02.535593', '2026-02-19 12:44:02.535593');

INSERT INTO `progress` (`id`, `name`, `progress_option_id`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019c75eea5ad7936b3917fd0de59a2cc, 'Beginner', 0x019c75eda9f87b539ff5376498e646b8, 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 12:45:00.206087', '2026-02-19 12:45:00.206087'),
(0x019c75eeb58b7056983bcea71ce5b596, 'Advanced', 0x019c75eda9f87b539ff5376498e646b8, 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 12:45:04.267021', '2026-02-19 12:45:04.267021'),
(0x019c75eed938718f8243ee2d7fd1443f, 'Pro', 0x019c75eda9f87b539ff5376498e646b8, 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 12:45:13.400618', '2026-02-19 12:45:13.400618');

INSERT INTO `member_progress` (`id`, `actor_id`, `progress_option_id`, `progress_id`, `branch_id`, `remarks`, `status`, `created_by`, `updated_by`, `created_at`, `updated_at`, `completed_at`) VALUES
(0x019c76489e297f86b68c3557330445ea, 0x8140f50da33f4569b76a20c348b77222, 0x019c75eda9f87b539ff5376498e646b8, 0x019c75eea5ad7936b3917fd0de59a2cc, 0x019ba279a6e67271893cffab220040a2, NULL, 'IN', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 14:23:16.530807', '2026-02-19 14:23:16.530807', NULL),
(0xc4f0f20d4fc8420bbab8dfc0b4c00ffc, 0x4382e175404d4bd797f0b379bd95f64a, 0x019c75eda9f87b539ff5376498e646b8, 0x019c75eea5ad7936b3917fd0de59a2cc, 0x019ba279a6e67271893cffab220040a2, NULL, 'IN', 0x75a6a919bfcb427c9ede78e2f49c960d, 0x75a6a919bfcb427c9ede78e2f49c960d, '2026-02-19 14:23:16.530807', '2026-02-19 14:23:16.530807', NULL);

INSERT INTO `member_progress_history` (`id`, `member_progress_id`, `progress_id`, `changed_at`) VALUES
(0x019c7e5b866e799f914e0c8b7077fd97, 0x019c76489e297f86b68c3557330445ea, 0x019c75eea5ad7936b3917fd0de59a2cc, '2026-02-21 04:00:53.353805'),
(0x019c7e5bd661744daf9dc7888ceb6cf5, 0x019c76489e297f86b68c3557330445ea, 0x019c75eeb58b7056983bcea71ce5b596, '2026-02-21 04:01:13.825269'),
(0x019c7e5be2ab7c7ba7f8a2087239e34b, 0x019c76489e297f86b68c3557330445ea, 0x019c75eea5ad7936b3917fd0de59a2cc, '2026-02-21 04:01:16.971780');


INSERT INTO `asset_categories` (`id`, `name`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba2f1a6e67271893cffab220040a1, 'Cardio Equipment', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-01-20 07:13:21.278244', '2026-01-20 07:13:21.278244'),
(0x019ba2f1a6e67271893cffab220040a2, 'Strength Equipment', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-01-20 07:13:21.278244', '2026-01-20 07:13:21.278244'),
(0x019ba2f1a6e67271893cffab220040a3, 'Yoga Equipment', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-01-20 07:13:21.278244', '2026-01-20 07:13:21.278244');

INSERT INTO `brands` (`id`, `name`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x0195574c878e79c09939e6a0d6323c2a, 'Life Fitness', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-01-20 07:13:21.278244', '2026-01-20 07:13:21.278244'),
(0x0195574c878e718b93540d41ca454c0e, 'Precor', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-01-20 07:13:21.278244', '2026-01-20 07:13:21.278244'),
(0x0195574c878e734394f79435f9927951, 'Technogym', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-01-20 07:13:21.278244', '2026-01-20 07:13:21.278244');

INSERT INTO `assets` (`id`, `branch_id`, `asset_category_id`, `name`, `manufactured_date`, `end_of_life`, `acquisition_date`, `status`, `remarks`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba2f5b6e67271893cffab220055b1, 0x019ba279a6e67271893cffab220040a2, 0x019ba2f1a6e67271893cffab220040a1, 'Treadmill', '2025-06-01', '2025-07-15', '2025-06-05', 'OPERATIONAL', 'Located near the window section.', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-01-20 07:13:36.305837', '2026-01-20 07:13:36.305837'),
(0x019ba2f5b6e67271893cffab220055b2, 0x019ba279a6e67271893cffab220040a2, 0x019ba2f1a6e67271893cffab220040a2, 'Leg Press Machine', '2025-08-15', '2025-09-01', '2025-08-16', 'DOWN', 'Located near the entrance.', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-01-20 07:13:36.305837', '2026-01-20 07:13:36.305837'),
(0x019ba2f5b6e67271893cffab220055b3, 0x019ba279a6e67271893cffab220040a2, 0x019ba2f1a6e67271893cffab220040a1, 'Stationary Bike', '2025-07-20', NULL, NULL, 'DECOMMISSIONED', 'Next to the treadmills.', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-02-12 16:43:00.000000', '2026-02-12 16:43:00.000000');

INSERT INTO `asset_brands` (`asset_id`, `brand_id`) VALUES
(0x019ba2f5b6e67271893cffab220055b1, 0x0195574c878e79c09939e6a0d6323c2a),
(0x019ba2f5b6e67271893cffab220055b2, 0x0195574c878e718b93540d41ca454c0e);

SET @now_minute = DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i:00');
SET @cur_week_rank = IF(MONTH(DATE_ADD(NOW(), INTERVAL 7 DAY)) <> MONTH(NOW()), -1, (DAYOFMONTH(NOW()) + 6) DIV 7);
SET @cur_day_of_week = WEEKDAY(NOW()) + 1;
SET @cur_month = MONTH(NOW());
SET @one_month_ago = DATE_SUB(@now_minute, INTERVAL 1 MONTH);
SET @one_year_ago = DATE_SUB(@now_minute, INTERVAL 1 YEAR);
SET @one_year_after = DATE_ADD(@now_minute, INTERVAL 1 YEAR);

INSERT INTO `maintenance_schedules` (`id`, `asset_id`, `name`, `start_date`, `interval_unit`, `interval_value`, `lead_time_hours`, `time_to_complete_hours`, `week_rank`, `day_of_week`, `month_of_year`, `is_active`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
-- Simple Schedules
(0x019ba2f9c6e67271893cffab220066c1, 0x019ba2f5b6e67271893cffab220055b1, 'Weekly Maintenance', @now_minute, 'WEEKS', 1, 24, 1, NULL, NULL, NULL, 1, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
(0x019ba32b5e897479850a65c0598000aa, 0x019ba2f5b6e67271893cffab220055b2, 'Daily Maintenance', @now_minute, 'DAYS', 1, 2, 1, NULL, NULL, NULL, 1, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
(0x3917b8aff5ed11f0bd300242ac140002, 0x019ba2f5b6e67271893cffab220055b1, 'Hourly Maintenance', @now_minute, 'HOURS', 1, 0, 0, NULL, NULL, NULL, 1, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
-- Advanced Schedules
(0x019ba2f9c6e67271893cffab2200aaa1, 0x019ba2f5b6e67271893cffab220055b1, 'Fresh Advanced Monthly', @now_minute, 'MONTHS', 1, 0, 2, @cur_week_rank, @cur_day_of_week, NULL, 1, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
(0x019ba2f9c6e67271893cffab2200aaa2, 0x019ba2f5b6e67271893cffab220055b2, 'Fresh Advanced Yearly', @now_minute, 'YEARS', 1, 0, 2, @cur_week_rank, @cur_day_of_week, @cur_month, 1, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
(0x019ba2f9c6e67271893cffab2200bbb1, 0x019ba2f5b6e67271893cffab220055b1, 'Old Advanced Monthly', @one_month_ago, 'MONTHS', 1, 0, 2, @cur_week_rank, @cur_day_of_week, NULL, 1, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
(0x019ba2f9c6e67271893cffab2200bbb2, 0x019ba2f5b6e67271893cffab220055b2, 'Old Advanced Yearly', @one_year_ago, 'YEARS', 1, 0, 2, @cur_week_rank, @cur_day_of_week, @cur_month, 1, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
-- Future Schedule
(0x019ba2f9c6e67271893cffab22009999, 0x019ba2f5b6e67271893cffab220055b1, 'Future Maintenance', @one_year_after, 'YEARS', 1, 24, 1, NULL, NULL, NULL, 1, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW());

INSERT INTO `supplies` (`id`, `branch_id`, `name`, `description`, `quantity`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019c21b46b0875df9a08c7a10ed983f0, 0x019ba279a6e67271893cffab220040a2, 'Gallon Drinking Water', 'Dispenser Refill', 0, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-02-03 04:13:18.002393', '2026-02-03 04:13:18.002393'),
(0x019c2caa28807436b2aafea1e4dc528c, 0x019ba279a6e67271893cffab220040a2, 'Equipment Wipes', 'Equipment Wipes', 0, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-02-05 07:17:54.961232', '2026-02-05 07:17:54.961232');

INSERT INTO `supplies_logs` (`id`, `supplies_id`, `name`, `remarks`, `quantity`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019c21c9cb277c48bcac03e5a6fa7924, 0x019c21b46b0875df9a08c7a10ed983f0, 'Gallon Drinking Water Restocked', NULL, 10, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-02-03 04:36:38.839501', '2026-02-03 04:36:38.839501'),
(0x019c21c9f9bf706db37d8c01ee611f92, 0x019c21b46b0875df9a08c7a10ed983f0, 'Gallon Drinking Water Used', NULL, -5, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-02-03 04:36:50.752159', '2026-02-03 04:36:50.752159');

INSERT INTO `report_types` (`id`, `name`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019c4120d55b7ac78b3134dcf3a9e69d, 'Negative', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-02-09 06:39:56.764291', '2026-02-09 06:50:36.639823'),
(0x019c4121866978b2a456ba590a1c5467, 'Positive', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-02-09 06:40:42.090416', '2026-02-09 06:40:42.090416');

INSERT INTO `reports` (`id`, `branch_id`, `actor_id`, `report_type_id`, `description`, `occurred_at`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019c412a055a78f2b2a85cc652587123, 0x019ba279a6e67271893cffab220040a2, 0x8140f50da33f4569b76a20c348b77222, 0x019c4120d55b7ac78b3134dcf3a9e69d, 'Member intentionally threw a dumbbell at a mirror', '2026-02-09 14:45:00.000000', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, '2026-02-09 06:49:58.875087', '2026-02-09 06:49:58.875087');

-- Other Expense Types
INSERT INTO `other_expense_types` (`id`, `name`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba279a6e67271893cffab220099bb, 'Office Maintenance', 0x8140f50da33f4569b76a20c348b77222, 0x8140f50da33f4569b76a20c348b77222, NOW(), NOW()),
(0x019ba279a6e67271893cffab220099cc, 'Marketing & Ads', 0x8140f50da33f4569b76a20c348b77222, 0x8140f50da33f4569b76a20c348b77222, NOW(), NOW());

-- Insert Other Expenses
INSERT INTO `other_expenses` (`id`, `amount`, `paid_at`, `branch_id`, `other_expense_id`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
    (0x019ba279a6e67271893cffab2200aabb, 1250.00, NOW(), 0x019ba279a6e67271893cffab220040a2, 0x019ba279a6e67271893cffab220099bb, 0x8140f50da33f4569b76a20c348b77222, 0x8140f50da33f4569b76a20c348b77222, NOW(), NOW());

-- Asset Maintenance
INSERT INTO `asset_maintenance` (`id`, `maintenance_date`, `due_date`, `completion_date`, `status`, `description`, `asset_id`, `maintenance_schedule_id`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
    (0x019ba279a6e67271893cffab220011bb, '2026-02-03 08:00:00', '2026-02-03 17:00:00', '2026-02-03 15:30:00', 'COMPLETED', 'Weekly routine inspection', 0x019ba2f5b6e67271893cffab220055b1, 0x019ba2f9c6e67271893cffab220066c1, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW());

-- Asset Maintenance Expenses
INSERT INTO `asset_maintenance_expenses` (`id`, `amount`, `paid_at`, `branch_id`, `asset_maintenance_id`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
    (0x019ba279a6e67271893cffab220022cc, 4500.00, '2026-02-03 16:00:00', 0x019ba279a6e67271893cffab220040a2, 0x019ba279a6e67271893cffab220011bb, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW());
-- Utility Types
INSERT INTO `utility_types` (`id`, `name`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
(0x019ba279a6e67271893cffab2200cc11, 'Electricity', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
(0x019ba279a6e67271893cffab2200cc22, 'Water', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
(0x019ba279a6e67271893cffab2200cc33, 'Internet', 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW());

-- Utility Expenses
INSERT INTO `utility_expenses` (`id`, `meter`, `amount`, `period`, `paid_at`, `branch_id`, `utility_type_id`, `created_by`, `updated_by`, `created_at`, `updated_at`) VALUES
-- Electricity bill for Jan 2026
(0x019ba279a6e67271893cffab2200dd11, 'E-992834', 1250.50, '2026-01-01', '2026-02-05 09:00:00', 0x019ba279a6e67271893cffab220040a2, 0x019ba279a6e67271893cffab2200cc11, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW()),
-- Water bill for Jan 2026
(0x019ba279a6e67271893cffab2200dd22, 'W-112233', 450.75, '2026-01-01', '2026-02-07 11:30:00', 0x019ba279a6e67271893cffab220040a2, 0x019ba279a6e67271893cffab2200cc22, 0xf520a8fb382443398bb43732c8a3f617, 0xf520a8fb382443398bb43732c8a3f617, NOW(), NOW());
COMMIT;