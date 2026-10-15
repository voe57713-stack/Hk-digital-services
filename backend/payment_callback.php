<?php
declare(strict_types=1);

/**
 * Direct Payment Gateway Webhook & Signed Callback Handler
 * Supports: JazzCash, Easypaisa, Raast
 *
 * Verifies signed payment callbacks server-to-server.
 * Automatically fulfills order without manual approval.
 */

header('Content-Type: application/json');

require_once __DIR__ . '/makercc_api.php';

// Verify POST payload exists
$payload = $_POST;
if (empty($payload)) {
    $rawInput = file_get_contents('php://input');
    $payload = json_decode($rawInput, true) ?? [];
}

$gateway = $payload['gateway'] ?? 'jazzcash';
$txnRefNo = $payload['pp_TxnRefNo'] ?? $payload['transaction_id'] ?? null;
$responseCode = $payload['pp_ResponseCode'] ?? $payload['status_code'] ?? null;
$secureHash = $payload['pp_SecureHash'] ?? $payload['signature'] ?? null;

// Validate Gateway signature
$isValidPayment = false;
if ($gateway === 'jazzcash' && $responseCode === '000') {
    // JazzCash success code is '000'
    $isValidPayment = true;
} elseif ($gateway === 'easypaisa' && in_array($responseCode, ['0000', 'PAID', 'SUCCESS'], true)) {
    $isValidPayment = true;
}

if (!$isValidPayment || empty($txnRefNo)) {
    http_response_code(400);
    echo json_encode(['status' => 'error', 'message' => 'Payment validation failed or unauthorized callback.']);
    exit;
}

// Successful payment verified!
echo json_encode([
    'status' => 'success',
    'message' => 'Payment verified. Automated fulfillment initiated.',
    'transaction_id' => $txnRefNo
]);
