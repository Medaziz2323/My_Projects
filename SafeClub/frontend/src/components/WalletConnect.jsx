import { useAccount, useConnect, useDisconnect } from "wagmi";

export default function WalletConnect() {
  const { isConnected, address } = useAccount();
  const { connect, connectors, error } = useConnect();
  const { disconnect } = useDisconnect();

  if (isConnected) {
    return (
      <div className="flex items-center gap-3">
        <span className="text-green-400 font-bold">
          {address.slice(0, 6)}...{address.slice(-4)}
        </span>
        <button
          onClick={() => disconnect()}
          className="bg-red-600 hover:bg-red-700 px-3 py-2 rounded"
        >
          Disconnect
        </button>
      </div>
    );
  }

  return (
    <button
      className="bg-purple-600 hover:bg-purple-700 px-4 py-2 rounded"
      onClick={() => connect({ connector: connectors[0] })}
    >
      Connect MetaMask
    </button>
  );
}
