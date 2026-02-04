// SPDX-License-Identifier: MIT
pragma solidity ^0.8.20;

import "@openzeppelin/contracts/utils/ReentrancyGuard.sol";
import "@openzeppelin/contracts/access/Ownable.sol";

contract SafeClubC is ReentrancyGuard, Ownable {

    constructor() Ownable(msg.sender) {}

    // ===== MEMBERS =====
    mapping(address => bool) public isMember;
    address[] public memberList;

    modifier onlyMember() {
        require(isMember[msg.sender], "Not a member");
        _;
    }

    function addMember(address _account) external onlyOwner {
        require(_account != address(0), "Zero address");
        require(!isMember[_account], "Already member");
        isMember[_account] = true;
        memberList.push(_account);
    }

    function memberCount() public view returns (uint) {
        return memberList.length;
    }

    // ===== PROPOSALS =====
    struct Proposal {
        address payable recipient;
        uint amount;
        string description;
        uint deadline;
        uint yesVotes;
        uint noVotes;
        bool executed;
        bool cancelled;
    }

    Proposal[] public proposals;
    mapping(uint => mapping(address => bool)) public hasVoted;

    // ===== EVENTS =====
    event ProposalCreated(
        uint id,
        address recipient,
        uint amount,
        string description,
        uint deadline
    );
    event Voted(uint id, address voter, bool support);
    event Cancelled(uint id);
    event Executed(uint id, address recipient, uint amount);

    // ===== TREASURY RECEIVE ETH =====
    receive() external payable {}
    function deposit() external payable {}

    // ===== CREATE PROPOSAL =====
    function createProposal(
        address payable _recipient,
        uint _amount,
        string calldata _description,
        uint _durationSeconds
    ) external onlyMember {
        require(_recipient != address(0), "Zero recipient");
        require(_durationSeconds > 0, "Duration must be > 0");
        require(address(this).balance >= _amount, "Treasury too small");

        proposals.push(
            Proposal({
                recipient: _recipient,
                amount: _amount,
                description: _description,
                deadline: block.timestamp + _durationSeconds,
                yesVotes: 0,
                noVotes: 0,
                executed: false,
                cancelled: false
            })
        );

        emit ProposalCreated(
            proposals.length - 1,
            _recipient,
            _amount,
            _description,
            block.timestamp + _durationSeconds
        );
    }

    // ===== VOTE =====
    function vote(uint _id, bool support) external onlyMember {
        Proposal storage p = proposals[_id];
        require(!p.cancelled, "Proposal cancelled");
        require(block.timestamp < p.deadline, "Voting closed");
        require(!hasVoted[_id][msg.sender], "Already voted");

        hasVoted[_id][msg.sender] = true;

        if (support) p.yesVotes++;
        else p.noVotes++;

        emit Voted(_id, msg.sender, support);
    }

    // ===== CANCEL PROPOSAL (ONLY OWNER) =====
    function cancel(uint _id) external onlyOwner {
        Proposal storage p = proposals[_id];
        require(!p.executed, "Already executed");
        require(!p.cancelled, "Already cancelled");

        p.cancelled = true;
        emit Cancelled(_id);
    }

    // ===== EXECUTE PROPOSAL =====
    function execute(uint _id) external nonReentrant {
        Proposal storage p = proposals[_id];

        require(!p.cancelled, "Cancelled proposal");
        require(!p.executed, "Already executed");
        require(block.timestamp >= p.deadline, "Voting still active");

        uint totalMembers = memberCount();
        require(totalMembers > 0, "No members exist");

        // Majority rule: yesVotes > 50% of members
        require(p.yesVotes * 2 > totalMembers, "Not majority approval");
        require(address(this).balance >= p.amount, "Not enough funds");

        p.executed = true;
        p.recipient.transfer(p.amount);

        emit Executed(_id, p.recipient, p.amount);
    }
}
