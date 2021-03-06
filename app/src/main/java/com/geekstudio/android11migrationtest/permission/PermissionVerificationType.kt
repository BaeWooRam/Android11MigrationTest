package com.geekstudio.android11migrationtest.permission

/**
 * 퍼미션 관련 검증 결과
 */
enum class PermissionVerificationType {
    NOT_INIT_LAUNCH,
    NOT_INIT_TARGET,
    NOT_INIT_PERMISSION_DATA,
    EMPTY_PERMISSION_DATA,
    NOT_INIT_PERMISSION_LISTENER,
    OK
}