import { ethers } from "hardhat";
import { expect } from "chai";
import { SecuritiesExchange } from "../typechain-types";
import { HardhatEthersHelpers } from "hardhat/types";

describe("SecuritiesExchange", function () {
  let exchange : SecuritiesExchange;
  let owner, addr1;

  beforeEach(async function () {
    const SecuritiesExchange = await ethers.getContractFactory("SecuritiesExchange");
    [owner, addr1] = await ethers.getSigners();
    exchange = await SecuritiesExchange.deploy();
    await exchange.waitForDeployment();
  });

  it("Should add a new security", async function () {
    await exchange.addSecurity("Stock A", ethers.parseEther("0.01"), 100);
    const security = await exchange.securities(0);
    expect(security.name).to.equal("Stock A");
    expect(security.price).to.equal(ethers.parseEther("0.01"));
    expect(security.totalSupply).to.equal(100);
  });

  it("Should allow purchase of securities", async function () {
    await exchange.addSecurity("Stock A", ethers.parseEther("0.01"), 100);
    await exchange.connect(addr1).purchaseSecurity(0, 10, {
      value: ethers.parseEther("0.1"),
    });
    const balance = await exchange.balanceOf(addr1.address, 0);
    expect(balance).to.equal(10);
  });

  it("Should allow exchange of securities", async function () {
    await exchange.addSecurity("Stock A", ethers.parseEther("0.01"), 100);
    await exchange.connect(addr1).purchaseSecurity(0, 10, {
      value: ethers.parseEther("0.1"),
    });

    await exchange.connect(addr1).exchangeSecurities(0, 5);
    const balance1 = await exchange.balanceOf(addr1.address, 0);
    const balance2 = await ethers.provider.getBalance(exchange);
    expect(balance1).to.equal(5);
    expect(balance2).to.equal(ethers.parseEther("0.05"));
  });
});
