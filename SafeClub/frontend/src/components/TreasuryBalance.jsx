import { useState } from "react";
import { ethers } from "ethers";
import { CONTRACT_ADDRESS } from "../constants";

export default function TreasuryBalance() {
  const [balance, setBalance] = useState("");

  const loadBalance = async () => {
    const provider = new ethers.JsonRpcProvider("http://127.0.0.1:8545");
    const bal = await provider.getBalance(CONTRACT_ADDRESS);
    setBalance(ethers.formatEther(bal));
  };

  return (
    <div className="bg-gray-900 rounded-xl p-6 shadow-glow border border-blue-700">
      <h2 className="text-2xl font-bold text-blue-300 mb-3">
        💰 Treasury Balance
      </h2>

      {balance === "" ? (
        <button
          className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded w-full"
          onClick={loadBalance}
        >
          Load Balance
        </button>
      ) : (
        <p className="text-blue-100 text-xl">
          {balance} ETH
        </p>
      )}
    </div>
  );
}
