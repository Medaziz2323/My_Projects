import { getAccount, readContract } from "wagmi/actions";
import { useState, useEffect } from "react";
import abi from "../abi/SafeClub.json";
import { CONTRACT_ADDRESS } from "../constants";
import { formatEther } from "ethers";

export default function ListProposals() {
  const [items, setItems] = useState([]);

  async function load() {
    const { chainId } = getAccount();
    if (!chainId) return; // wallet not connected yet

    let proposals = [];
    for (let i = 0; i < 5; i++) {
      try {
        let p = await readContract({
          address: CONTRACT_ADDRESS,
          abi,
          functionName: "proposals",
          args: [i],
        });
        proposals.push({ id: i, ...p });
      } catch {}
    }
    setItems(proposals);
  }

  useEffect(() => {
    load();
  }, []);

  return (
    <div className="space-y-4 bg-gray-900 p-6 rounded-xl shadow-lg mt-6">
      <h2 className="text-xl text-purple-400 font-bold">Proposals</h2>

      {items.map((p) => (
        <div key={p.id} className="bg-gray-800 p-4 rounded-lg">
          <p>ID: {p.id}</p>
          <p>Recipient: {p.recipient}</p>
          <p>Amount: {formatEther(p.amount)} ETH</p>
          <p>{p.description}</p>
          <p className="text-yellow-400">Yes: {p.yesVotes} | No: {p.noVotes}</p>
        </div>
      ))}
    </div>
  );
}
