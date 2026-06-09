const base = process.env.OPTGUARD_API_URL || 'https://optguard-api.onrender.com/api';
const origin = process.env.OPTGUARD_ORIGIN || 'https://opt-guard.vercel.app';
const email = `prod-smoke-${Date.now()}@example.com`;
const password = 'TestPassword123!';

async function request(path, options = {}) {
  const response = await fetch(`${base}${path}`, {
    ...options,
    headers: {
      'content-type': 'application/json',
      origin,
      ...(options.token ? { authorization: `Bearer ${options.token}` } : {}),
      ...(options.headers || {})
    }
  });
  const text = await response.text();
  const body = parseBody(text);
  if (!response.ok) {
    throw new Error(`${options.method || 'GET'} ${path} failed ${response.status}: ${text}`);
  }
  return body;
}

const auth = await request('/auth/register', {
  method: 'POST',
  body: JSON.stringify({ fullName: 'Production Smoke Test', email, password })
});
const token = auth.token;

const profile = await request('/profile', {
  method: 'PUT',
  token,
  body: JSON.stringify({
    schoolName: 'Sample University',
    dsoName: 'Avery DSO',
    dsoEmail: 'avery.dso@example.com',
    sevisId: 'N0000000000',
    visaStatus: 'F-1',
    passportExpiryDate: '2030-12-31'
  })
});

const optRecord = await request('/opt-record', {
  method: 'PUT',
  token,
  body: JSON.stringify({
    optStartDate: '2026-07-01',
    optEndDate: '2027-06-30',
    stemStartDate: '2027-07-01',
    stemEndDate: '2029-06-30',
    eadStartDate: '2026-07-01',
    eadEndDate: '2027-06-30',
    unemploymentDaysUsed: 0,
    status: 'INITIAL_OPT'
  })
});

const deadlines = await request('/deadlines/generate', { method: 'POST', token, body: '{}' });
const employer = await request('/employers', {
  method: 'POST',
  token,
  body: JSON.stringify({
    employerName: 'Launch Test Labs',
    ein: '12-3456789',
    eVerifyNumber: '123456789',
    jobTitle: 'Data Analyst',
    employmentStartDate: '2026-07-15',
    worksiteAddress: '100 Market St, San Francisco, CA',
    supervisorName: 'Jordan Lee',
    supervisorEmail: 'jordan.lee@example.com',
    current: true
  })
});
const templates = await request('/email-templates', { token });
const dashboard = await request('/dashboard/summary', { token });
const login = await request('/auth/login', {
  method: 'POST',
  body: JSON.stringify({ email, password })
});

console.log(JSON.stringify({
  email,
  registered: Boolean(auth.token),
  loginWorks: Boolean(login.token),
  profileId: profile.id,
  optRecordId: optRecord.id,
  deadlines: deadlines.length,
  employerId: employer.id,
  templates: templates.length,
  dashboardStatus: dashboard.currentStatus,
  upcomingDeadlines: dashboard.upcomingDeadlines?.length ?? 0
}, null, 2));

function parseBody(text) {
  try {
    return text ? JSON.parse(text) : null;
  } catch {
    return text;
  }
}
