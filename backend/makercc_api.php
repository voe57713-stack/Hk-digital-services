<?php
declare(strict_types=1);

/**
 * MakerCC Supplier API Integration Class
 * Supplier Website: https://makercc.shop/
 * Official Endpoint: https://makercc.shop/api/v1/index.php
 *
 * Implements documented actions:
 *  - services : Retrieve supplier products, prices, and available stock
 *  - add      : Submit purchase for an account / service
 *  - status   : Check fulfillment status and delivery details
 *  - balance  : Check supplier wallet balance
 */
class MakerCcApi {
    private string $endpoint;
    private string $apiKey;

    public function __construct(string $apiKey, string $endpoint = 'https://makercc.shop/api/v1/index.php') {
        $this->apiKey = trim($apiKey);
        $this->endpoint = trim($endpoint);
    }

    /**
     * Check MakerCC Wallet Balance (Account 1)
     */
    public function getBalance(): array {
        return $this->request(['action' => 'balance']);
    }

    /**
     * Retrieve MakerCC Services List
     */
    public function getServices(): array {
        return $this->request(['action' => 'services']);
    }

    /**
     * Purchase a product from MakerCC
     */
    public function addOrder(string $serviceId, string $targetEmail, int $quantity = 1): array {
        return $this->request([
            'action'   => 'add',
            'service'  => $serviceId,
            'link'     => $targetEmail,
            'quantity' => $quantity
        ]);
    }

    /**
     * Check status and credentials of a supplier order
     */
    public function getOrderStatus(string $orderId): array {
        return $this->request([
            'action' => 'status',
            'order'  => $orderId
        ]);
    }

    private function request(array $params): array {
        $params['key'] = $this->apiKey;

        $ch = curl_init();
        curl_setopt_array($ch, [
            CURLOPT_URL            => $this->endpoint,
            CURLOPT_POST           => true,
            CURLOPT_POSTFIELDS     => http_build_query($params),
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_SSL_VERIFYPEER => true,
            CURLOPT_CONNECTTIMEOUT => 15,
            CURLOPT_TIMEOUT        => 30,
            CURLOPT_USERAGENT      => 'HamadHassanStore/1.0'
        ]);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $error    = curl_error($ch);
        curl_close($ch);

        if ($response === false) {
            return ['success' => false, 'error' => "cURL connection error: {$error}"];
        }

        $decoded = json_decode($response, true);
        if (!is_array($decoded)) {
            return ['success' => false, 'error' => "Invalid response received from supplier API. HTTP Code: {$httpCode}"];
        }

        if (isset($decoded['error'])) {
            return ['success' => false, 'error' => $decoded['error']];
        }

        return ['success' => true, 'data' => $decoded];
    }
}
