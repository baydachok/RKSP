// SPDX-License-Identifier: MIT

pragma solidity >=0.8.2 <0.9.0;

contract SecuritiesExchange {
    // Байдак Данила Михайлович ИКБО-01-21
    address public owner;
    mapping(address => mapping(uint256 => uint256)) public balances;
    mapping(uint256 => Security) public securities;
    uint256 public securityCount;

    struct Security {
        string name;
        uint256 price;
        uint256 totalSupply;
    }

    event SecurityAdded(uint256 indexed securityId, string name, uint256 price, uint256 totalSupply);
    event SecurityPurchased(address indexed buyer, uint256 indexed securityId, uint256 amount);
    event SecurityExchanged(address indexed seller, uint256 indexed securityId, uint256 amount);

    modifier onlyOwner() {
        require(msg.sender == owner, "Only the owner can call this function");
        _;
    }

    constructor() {
        owner = msg.sender;
    }

    function addSecurity(string memory name, uint256 price, uint256 totalSupply) public onlyOwner {
        securities[securityCount] = Security(name, price, totalSupply);
        emit SecurityAdded(securityCount, name, price, totalSupply);
        securityCount++;
    }

    function purchaseSecurity(uint256 securityId, uint256 amount) public payable {
        Security storage security = securities[securityId];
        require(amount > 0, "Invalid amount");
        require(security.totalSupply >= amount, "Not enough supply");

        uint256 totalCost = security.price * amount;
        require(msg.value >= totalCost, "Insufficient funds sent");

        balances[msg.sender][securityId] += amount;
        security.totalSupply -= amount;

        if (msg.value > totalCost) {
            payable(msg.sender).transfer(msg.value - totalCost);
        }

        emit SecurityPurchased(msg.sender, securityId, amount);
    }

    function exchangeSecurities(uint256 securityId, uint256 amount) public {
        require(balances[msg.sender][securityId] >= amount, "Not enough balance");
        require(amount > 0, "Invalid amount");

        balances[msg.sender][securityId] -= amount;
        Security storage security = securities[securityId];
        security.totalSupply += amount;
        payable(msg.sender).transfer(security.price * amount);

        emit SecurityExchanged(msg.sender, securityId, amount);
    }

    function balanceOf(address account, uint256 securityId) public view returns (uint256) {
        return balances[account][securityId];
    }

    function withdrawBalance() public onlyOwner {
        uint256 balance = address(this).balance;
        require(balance > 0, "No balance to withdraw");
        payable(owner).transfer(balance);
    }

}
