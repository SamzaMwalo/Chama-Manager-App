const { onRequest } = require("firebase-functions/v2/https");
const { onSchedule } = require("firebase-functions/v2/scheduler");
const admin = require("firebase-admin");

admin.initializeApp();

exports.scheduleContributionReminders = onSchedule("0 8 * * 1", async () => {
  const db = admin.firestore();
  const contributions = await db.collection("contributions").get();
  const batch = db.batch();
  const now = new Date().toISOString();

  contributions.forEach((doc) => {
    const data = doc.data();
    if (!data.chamaId || !data.memberId) return;

    const ref = db.collection("notifications").doc();
    batch.set(ref, {
      chamaId: data.chamaId,
      title: "Contribution reminder",
      body: `Contribution due by ${data.dueOn || "the next cycle"}.`,
      type: "CONTRIBUTION_REMINDER",
      targetMemberId: data.memberId,
      createdAt: now,
      read: false
    });
  });

  await batch.commit();
});

exports.scheduleLoanAlerts = onSchedule("0 9 * * *", async () => {
  const db = admin.firestore();
  const loans = await db.collection("loans").where("status", "in", ["APPROVED", "ACTIVE", "OVERDUE"]).get();
  const batch = db.batch();
  const now = new Date().toISOString();

  loans.forEach((doc) => {
    const data = doc.data();
    if (!data.chamaId || !data.memberId) return;

    const ref = db.collection("notifications").doc();
    batch.set(ref, {
      chamaId: data.chamaId,
      title: "Loan due alert",
      body: `Loan due date: ${data.dueDate || "upcoming"}.`,
      type: "LOAN_DUE_ALERT",
      targetMemberId: data.memberId,
      createdAt: now,
      read: false
    });
  });

  await batch.commit();
});

exports.mpesaCallback = onRequest(async (req, res) => {
  const db = admin.firestore();
  const callback = req.body?.Body?.stkCallback;

  await db.collection("mpesaLogs").add({
    payload: req.body,
    resultCode: callback?.ResultCode ?? null,
    resultDesc: callback?.ResultDesc ?? null,
    createdAt: new Date().toISOString()
  });

  res.status(200).send({ status: "received" });
});
