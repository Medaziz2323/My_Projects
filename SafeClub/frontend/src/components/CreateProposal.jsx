import { writeContract, getAccount } from "wagmi/actions";
import { useState } from "react";
import abi from "../abi/SafeClub.json";
import { CONTRACT_ADDRESS } from "../constants";

export default function CreateProposal() {
  const [recipient, setRecipient] = useState("");
  const [amount, setAmount] = useState("");
  const [desc, setDesc] = useState("");

  const submit = async () => {
    const { address: caller } = getAccount();
    if (!caller) return alert("Connect wallet first!");
    if (!recipient || !amount) return alert("Missing fields");

    await writeContract({
      account: caller,
      address: CONTRACT_ADDRESS,
      abi,
      functionName: "createProposal",
      args: [
        recipient,
        BigInt(Number(amount) * 1e18),
        desc,
        300, // voting seconds
      ],
    });

    alert("Proposal Created!");
    setRecipient("");
    setAmount("");
    setDesc("");
  };

  return (
    <div className="bg-gray-900 p-5 rounded-xl shadow-glow space-y-3">
      <h2 className="text-xl font-bold text-purple-400">Create Proposal</h2>

      <input
        placeholder="Recipient 0x.."
        className="bg-gray-700 px-3 py-2 rounded w-full"
        value={recipient}
        onChange={(e) => setRecipient(e.target.value)}
      />
      <input
        placeholder="Amount ETH"
        className="bg-gray-700 px-3 py-2 rounded w-full"
        value={amount}
        onChange={(e) => setAmount(e.target.value)}
      />
      <input
        placeholder="Description"
        className="bg-gray-700 px-3 py-2 rounded w-full"
        value={desc}
        onChange={(e) => setDesc(e.target.value)}
      />

      <button
        onClick={submit}
        className="bg-blue-500 px-4 py-2 rounded font-bold hover:bg-blue-600 w-full"
      >
        Submit
      </button>
    </div>
  );
}
