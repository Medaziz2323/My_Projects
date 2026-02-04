import { buildModule } from "@nomicfoundation/hardhat-ignition/modules";

export default buildModule("SafeClubModule", (m) => {
  const safeclub = m.contract("SafeClubC");
  return { safeclub };
});
