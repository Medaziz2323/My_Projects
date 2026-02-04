import { writeContract, getAccount } from "wagmi/actions";
import { useState } from "react";
import abi from "../abi/SafeClub.json";
import { CONTRACT_ADDRESS } from "../constants";

export default function AddMember() {
  const [addr, setAddr] = useState("");

  const submit = async () => {
    const { address: caller } = getAccount();
    if (!caller) return alert("Connect wallet first!");
    if (!addr) return alert("Enter member address");

    await writeContract({
      account: caller,
      address: CONTRACT_ADDRESS,
      abi,
      functionName: "addMember",
      args: [addr],
    });

    alert("Member added!");
    setAddr("");
  };

  return (
    <div className="bg-gray-900 p-5 rounded-xl shadow-glow space-y-3">
      <h2 className="text-xl font-bold text-purple-400">Add Member</h2>

      <input
        className="bg-gray-700 px-3 py-2 rounded w-full"
        placeholder="0x Address"
        value={addr}
        onChange={(e) => setAddr(e.target.value)}
      />

      <button
        onClick={submit}
        className="bg-purple-500 px-4 py-2 rounded font-bold hover:bg-purple-600 w-full"
      >
        Add
      </button>
    </div>
  );
}
