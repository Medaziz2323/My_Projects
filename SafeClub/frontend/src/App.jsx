import { useAccount, useConnect, useDisconnect } from "wagmi";
import { injected } from "wagmi/connectors";

import AddMember from "./components/AddMember";
import CreateProposal from "./components/CreateProposal";
import ListProposals from "./components/ListProposals";
import TreasuryBalance from "./components/TreasuryBalance";

export default function App() {
  const { address, isConnected } = useAccount();
  const { connect, connectors } = useConnect();
  const { disconnect } = useDisconnect();

  return (
    <div className="min-h-screen bg-gray-950 text-white">
      <nav className="flex justify-between items-center px-8 py-4 bg-black/30 border-b border-purple-700/40">
        <h1 className="text-3xl font-bold text-purple-400">🟣 SafeClub</h1>
        {!isConnected ? (
          <button
            onClick={() => connect({ connector: connectors[0] })}
            className="px-4 py-2 bg-purple-600 hover:bg-purple-700 rounded"
          >
            Connect MetaMask
          </button>
        ) : (
          <button
            onClick={() => disconnect()}
            className="px-4 py-2 bg-red-600 hover:bg-red-700 rounded"
          >
            Disconnect
          </button>
        )}
      </nav>

      <div className="p-10 space-y-10">
        {!isConnected && (
          <p className="text-center text-gray-300">
            Connect wallet to manage the DAO ⚡
          </p>
        )}

        {isConnected && (
          <>
            <p className="text-center text-green-300">
              Connected as <span className="text-green-400 font-bold">{address}</span>
            </p>

            <TreasuryBalance />

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <AddMember />
              <CreateProposal />
            </div>

            <ListProposals />
          </>
        )}
      </div>
    </div>
  );
}
