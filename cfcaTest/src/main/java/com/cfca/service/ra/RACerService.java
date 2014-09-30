package com.cfca.service.ra;

import java.util.List;

import cfca.ra.common.vo.request.CertServiceRequestVO;
import cfca.ra.common.vo.request.QueryRequestVO;
import cfca.ra.common.vo.response.CertServiceResponseVO;
import cfca.ra.common.vo.response.QueryResponseListVO;
import cfca.ra.common.vo.response.QueryResponseVO;
import cfca.ra.toolkit.CFCARAClient;
import cfca.ra.toolkit.exception.RATKException;

/**
 * @Project: ra35client
 * @Author zy
 * @Company: JL
 * @Create Time: 2014年9月22日 下午1:31:40
 */
public class RACerService {



	public static void main(String[] args) {
		String p10 = "";
		p10 = "MIICgTCCAWkCAQAwPjELMAkGA1UEBhMCQ04xFTATBgNVBAoMDENGQ0EgVEVTVCBDQTEYMBYGA1UEAwwPY2VydFJlcXVpc2l0aW9uMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvG0ltadh8xC+HuYy+MqQXQBfOOqa4EbncWFoitWhdkv8ubHDPfS3xP65IeU9lFI2J1k4Kq5gHNK3ocd0TWbhb1xmHnZbzA1k8AH16E3dUtNq7EFwoTVAZatJ/sdKj52V0m/j0yovG1ovgm0UKwyVDsSNHcbFmuJOaaT6Tl1R6KJQ/VH/kTX4AWtWN/YCv0gwm5pX48eWX4+D2wsmdk/uRhQYH0vV76ouQ2g6XnAvqTGduqPtrDXyIZOahTJ7PTcKZ/RNgKFal4zNjafme8ecOfhJNKWAkFJk+n5QZzlophVZq0992AVOe++btxS5xeiF2gjRKJjNIjNAYLvtDXQBgQIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQB6oJh9E+8fPnfO/a2D/J+UznmY1Eon2y27QhVZ7cP0p3Y2w2DBbcb8nbDhoVaQONESUpUbMHnvnRS6EHkno7/u39J5veDz24CGk/jRGPXsAicaabWQlaAk6IJw6f9P8TJKT1BPYcFcnvpS+fR2Zpe2lv8LEbj13BNm6aA3ZqmUxPLFbHC9FuoD5eWOUhic6v4+8CnDLi9z0qBiyineRg6IO1cLK1cTHudDEDEbENojA2wqesbK84QANFy39YOz1A0QSyRyXBtrAwt2MegzVV/TywjtcfFqTSbgxNQ5mnNkk1fTPqBohs9BGUiOUgCSbdISoEXQz1ZL9b0Cs7F15PBN";
		p10 = "MIICgTCCAWkCAQAwPjELMAkGA1UEBhMCQ04xFTATBgNVBAoMDENGQ0EgVEVTVCBDQTEYMBYGA1UEAwwPY2VydFJlcXVpc2l0aW9uMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk97K8+DMB7vQRJuOLu3WMwudSIUdtiHq4ck5SDZIrvVv8D85hLPCHFCjMewQg5zqCiIy7Ciz3pDIMwH+Mj/XkUAAqkeNwfsLpoIjLAGfheBn71AbJOhjGDv1uLCELacQ7YwA5rWfAwA1H+jU/p2cxVyHeafDSk4+0RwqPMrqXh/LMiTwcDrrUNMmwN0Mx5GwykyOYhOkkD1B7JCjAw36288yrMG4bZElzABhjPZN5R8sk/9qZ8E2BEtHIA5e1Eo7Uvh+eb+dEgWRGUdmwO6bzQAQa9DCw8ODr5APMTWLGP39eZlpGWStoWQiI4oWp494UCQdBmVxH05dlR0DlX7tjQIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQAhuklKVigLAmEfZjtFYztEHn986dyTJSrIjMDkqYhIKgV7nVhPg7NpEiYmPJglM+fmyOHWiriVooWY+YZgDpMXV60H3e4JkBS10VfS3be5PLiQkjJIwuJH+vRWq9Q2rOqFtNdznPznsSb+ckG7wzEWrDeGnEsoODD3tUy7hz68jKXWVBD0RzKYJYHD+6sqCoX76aekWbfH8VUCOVNk1pwdZxKsOZsxcbxFoqX0UfQHsjKJwwqtwCfkJTH6dO711pP5weCAyNxFJLHp6csMPpsCmjxS7h6QlA/LH7o1x8lgpo23kfvHx6qvS6B2fuLb1R934Z9M2DGXny4GpTA6DnQu";
		p10 = "MIICgTCCAWkCAQAwPjELMAkGA1UEBhMCQ04xFTATBgNVBAoMDENGQ0EgVEVTVCBDQTEYMBYGA1UEAwwPY2VydFJlcXVpc2l0aW9uMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1vK6oEHkB4fJlMZAxOr28q8MC3RwvD5EJovKl3ULl9miWWkbc1+6eUcu34PtVknKGpmS/ODuqaVShmLrcmgNuCux+UMwHTfF1tuvFtPhLEETZeMUhKlHuoDqgrH43/0lv79qgkG2JO1Jc6lhf1bBxycl6y6jZmA3nzIlf/D5Sfm23g216rgXYsHLyUK1ZuVBsM04sRgLexJ8Ho5ur8GXfHPD1MANCeoJZqdyT/P0LKEEB/f/R2HzNWnk/6QEJxv+APmVXU8rtgcs4rN7kCmAXUYfj2635NV+941ZUqSq7fHVACxJ6CA1dnYHvSzWeYV0bEGB/PV9wkNRdWgEPPvSaQIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQARGxzdOggotuYKPtfmdSR5MiG0og103WhP9pKhbPQF0GZ6Mk/I7z+maOLuSAnRnKsbeTijJHXXti44bWEhufrpJ01XBIQ/F/imalFTmv09ik+ncEfgbIbzcFTb9jsdfzLTj5uV7it66YRgaaWQ3hf5G2cunPSSaXKfJpHN63TNzOHvbPdRtBLnpYJoFRfiEte3Zvij9S8JbkQ5hnJl7S9mh0ltuYwbIMm2URktOe5R/FKcvOZ4bRbPzknn7Q5BtAG4kNO0jmUmD6OB/q8kwekXcc8dH2jP2dcVMI9g4f6cNkMi43LGf54QfYiJdxHsZYQnav0gCLdDbSVAk9rxJqoD";
		
		System.out.println("========证书申请========");
		reqCer(p10);

//		String dn = "CN=051@ktTEST003@Z120009230960890@11,OU=Individual-1,OU=Local RA,O=CFCA TEST CA,C=CN";
//		System.out.println("========证书吊销========");
//		deactivateCer(dn);
		
		
	}
	
	
	private static CFCARAClient createCFCARAClient() {

		// 连接超时时间 毫秒
		int connectTimeout = 3000;
		
		// 读取超时时间 毫秒
		int readTimeout = 30000;

		// 服务器ip（socket、ssl socket方式）
		String ip = "172.16.230.171";
		
		// 服务器端口（socket、ssl socket方式）
		int port = 9040;
		
		CFCARAClient client = new CFCARAClient(ip, port, connectTimeout, readTimeout);
		
		return client;
	}

	/**
	 * 吊销
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月22日 下午1:23:18
	 * @param dn
	 */
	public static void deactivateCer(String dn) {
		// String locale = "zh_CN";
		// String dn =
		// "cn=05@testName@Z1234567890@1,ou=Individual-1,ou=Local RA,o=CFCA TEST CA,c=cn";
		try {
			CFCARAClient client = createCFCARAClient();

			CertServiceRequestVO certServiceRequestVO = new CertServiceRequestVO();
			certServiceRequestVO.setTxCode("2901");
			// certServiceRequestVO.setLocale(locale);
			certServiceRequestVO.setDn(dn);

			CertServiceResponseVO certServiceResponseVO = (CertServiceResponseVO) client.process(certServiceRequestVO);

			System.out.println(certServiceResponseVO.getResultCode());
			System.out.println(certServiceResponseVO.getResultMessage());
		} catch (RATKException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 申请证书
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月22日 下午1:23:31
	 * @param userName
	 * @param p10
	 */
	public static CertServiceResponseVO reqCer(String p10) {
		
		CertServiceResponseVO  certServiceResponseVO = null;
		
		// String locale = "zh_CN";
		String certType = "1";
		String customerType = "1";
		String userName = "ktTEST003";
		// String userNameInDn = "testName";
		// String userIdent = "Z1234567890";
		String identType = "Z";
		String identNo = "120009230960890";
		// String keyAlg = "RSA";
		// String keyLength = "2048";
		String branchCode = "678";
		// String email = "test@test.test";
		// String phoneNo = "12345678";
		// String address = "address";
		// String duration = "24";
		// String endTime = "20150101000000"; //
		// endTime与duration同时非空时，证书截止时间以endTime为准，duration作为证书默认有效期记入数据库
		// String addIdentNoExt = "false";
		// String addEmailExt = "false";
		// String selfExtValue = "extValue";
		// String p10 =
		// "MIICUjCCAToCAQAwDzENMAsGA1UEAwwEQ0ZDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAJO64nej6v5MXvbUDTfjRQdtyYDczksFePz8C+ae2T2d8vLviTwnaWRPTKBWOqaVPfkBsvnRCAZlUuyF1u8FYqpMumVMEHX0UDXcBxvtyy88+RbxX10XhueFyg9yFG2j1YpkamGHyZdBzd1v8x+jRxZ2JP82h5G9KMzMKRe2XCdKWCAzlNG11R5opHtXlwH6Ob4hMhIO93EAraxj0og+6OtLuPMnY2ZIaDJC1+WA0v7l0jGN5y/lWWYn4T8kJ8r9Yn4hrLtHSpt4aexWAFPC0+yFLBUIYnDjfLyN0IyblNsnnYWmnje3YvyzaVwXYGtdIWjR+kfMK3DRZ/MZCP9nY6ECAwEAATANBgkqhkiG9w0BAQUFAAOCAQEAj7okFFSiVJTCo65okPIB0XcKC6prOIpAE8Az62RSdiM6/yqv3M8bFSaJo7wHR5Ch5+dYmyYnP5nwU/2dFF7vcsjiHLsv5pFg+v7JnILfCfxg+Tb5ofxvkHy23Tpz16iFj6lGqoEQwTETjOwZBWSEpDZTCz/FYVTCWB6kM36p63bKSuAFCUmCaK+XYWQfJ3Zl2OkqKzrmi73U5okJaWksmtrTgAGKZfDW7FnZntaQhQHfy2XE9qCh50uOM1SnQoGQa+ihq3CbVt1a5Wj7KXbgzsIdhHv2kAaTBu5CWDbrdMPtZtOz9dKnbIr2A6aJNIbT8lyuZIe0eG/cXNoKxfrRIg==";
		// p10 =
		// "MIICgTCCAWkCAQAwPjELMAkGA1UEBhMCQ04xFTATBgNVBAoMDENGQ0EgVEVTVCBDQTEYMBYGA1UEAwwPY2VydFJlcXVpc2l0aW9uMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuggGHng+mM752lUS466dLKoJnHRgxxYjtTYM2cvLW3FduCGhdKT8Rfa2xSW1pKr97nxWFo5BVwWNheWRweQsm3VUgbmO9J4mX+PR/R9V/foYTEHAF8lsxZewTSJZ5BRUAnNqWaMsOx8CVGrTcaFnZuF03QkJvzg45kSZlG+/GGmS7YSj1m9Z6t0gJNQHbeF+cRyDn4mjngHhUIaMcNOiEtKNtoCfVBlvoougT7XSSjWvHoGz/3XYbcQeUWGHKIR+dvwneFA6Bu0tL8v8u+qQf82+la821tSdgwKFipgbCwpINgUkuMx3YUiEDrT6pJwUjPdx50zqh2qQ/U3E2W+7LQIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQBQqlYpQx/ko7daJOULS91KlTDiCJPz6z/8zHSRjtDLR2GPoFaOYm0/Ku3E34JvGo7+h4IotCydxib3UAyGl24w3xLuI45m0OagawdaJQHG9wv0bXK2qnALwGZw/tUwg2E8oYYenZKq8+6Pmvp1ghWV3gj25hWa924+uPebrrNaivLKYJHs0Mlm4QlriNoJpf6PAzq8hNc+hI4bhnzyaQ8Ka/YpddmT3eepGgPASC7aAfLX61dG8DFCvRbtBIPd2gmdIBvoxu5vDUfRU80oeCDxK2rxLPdXc262XJm2rR2q5S1Fdc6MPN26caCj+yM8tok9n4dGhfKb6ZeXg74F0pNB";
		try {
			CFCARAClient client = createCFCARAClient();
			CertServiceRequestVO certServiceRequestVO = new CertServiceRequestVO();
			certServiceRequestVO.setTxCode("1101");
			// certServiceRequestVO.setLocale(locale);
			certServiceRequestVO.setCertType(certType);
			certServiceRequestVO.setCustomerType(customerType);
			certServiceRequestVO.setUserName(userName);
			// certServiceRequestVO.setUserNameInDn(userNameInDn);
			// certServiceRequestVO.setUserIdent(userIdent);
			certServiceRequestVO.setIdentType(identType);
			certServiceRequestVO.setIdentNo(identNo);
			// certServiceRequestVO.setKeyLength(keyLength);
			// certServiceRequestVO.setKeyAlg(keyAlg);
			certServiceRequestVO.setBranchCode(branchCode);
			// certServiceRequestVO.setEmail(email);
			// certServiceRequestVO.setPhoneNo(phoneNo);
			// certServiceRequestVO.setAddress(address);
			// certServiceRequestVO.setDuration(duration);
			// certServiceRequestVO.setEndTime(endTime);
			// certServiceRequestVO.setAddIdentNoExt(addIdentNoExt);
			// certServiceRequestVO.setAddEmailExt(addEmailExt);
			// certServiceRequestVO.setSelfExtValue(selfExtValue);
			certServiceRequestVO.setP10(p10);

			 certServiceResponseVO = (CertServiceResponseVO) client.process(certServiceRequestVO);

			System.out.println(certServiceResponseVO.getResultCode());
			System.out.println(certServiceResponseVO.getResultMessage());
			if (CFCARAClient.SUCCESS.equals(certServiceResponseVO.getResultCode())) {
				System.out.println(certServiceResponseVO.getDn());
				System.out.println(certServiceResponseVO.getSequenceNo());
				System.out.println(certServiceResponseVO.getSerialNo());
				System.out.println(certServiceResponseVO.getStartTime());
				System.out.println(certServiceResponseVO.getEndTime());
				System.out.println(certServiceResponseVO.getSignatureCert());
				System.out.println(certServiceResponseVO.getEncryptionCert());
				System.out.println(certServiceResponseVO.getEncryptionPrivateKey());
			}
		} catch (RATKException e) {
			e.printStackTrace();
		}
		
		return certServiceResponseVO;
	}

	/**
	 * 证书更新
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月22日 下午1:25:25
	 * @param dn
	 * @param p10
	 */
	public static void updateCer(String dn, String p10) {
		// String locale = "zh_CN";
		// String dn =
		// "cn=05@testName@Z1234567890@1,ou=Individual-1,ou=Local RA,o=CFCA TEST CA,c=cn";
		// String keyLength = "2048";
		// String duration = "24";
		// String endTime = "20150101000000"; //
		// endTime与duration同时非空时，证书截止时间以endTime为准，duration作为证书默认有效期记入数据库
		// String useOldKey = "true";
		// String p10 =
		// "MIIDPzCCAicCAQAwPjELMAkGA1UEBhMCQ04xFTATBgNVBAoMDENGQ0EgVEVTVCBDQTEYMBYGA1UEAwwPY2VydFJlcXVpc2l0aW9uMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApSu+v/HezX43k/BQfQNnmuXpxed3iF65hDga3W9c3wh+7ukqGxwRyqYM0gPp1HBdavx3sDGKHr3SOtFJUxFvj4xJ2iyq3gKjtB+L3gbnJ0ep0RgqTqQepKSrTt8pYjkI0hkhHyYqzcwr20qCMDgIWLwj95Gr/rhoZPzvZIgW8yheSmlLvTFdjQdHBLru7pGALw5QlHJDR1ucVnOzAWLMmO2AOagMKjgm7IHjMztzObJuFvlOfoEX7b33TIIRqMUqIJ5xiiTWH5q5hWQF/hFDI23d1zHXeEZwmw9n5stIof86xuQe09le0Dhy1dylP0HtfGC2n37o2TxsbDtsApz07wIDAQABoIG7MBMGCSqGSIb3DQEJBxMGMTExMTExMIGjBgkqhkiG9w0BCT8EgZUwgZICAQEEgYwwgYkCgYEA6RZ6WtoBiZZzRgicGZ1SK7gCXrEQt20BIT6/Dpn/MjXVVI9Pd0OLYpXIR5CesbsWc1IwBnVvRj2cpfx9RrZO1AD65rglTjAUCUgxhVSC/qhH5E/Z/cY0zq6yKseHfYHh6ino0zhATbToS97hLLppAMsX1iJ152mPGMRNfviRRGcCAwEAATANBgkqhkiG9w0BAQUFAAOCAQEAXaJrnjjwH4pWlLAlNpSpay6BzB8bMLrJrkiMgERxAOA293uzELdpkVVQnjRMyCXJv8DYYnaMk4zPn67lt4IXsBE7EnS2xL99/8mg47tUALnxnvGWdENpjElSyXVQ9BMVg/mCuIqMgFb607iroejcJKYkz3YA+Lk3SEa6p1rHopgXo8LFh5cmEg1bdoMy1SBRIP01g/t/0vl3XinmHRikm030PTaUx5O15f2GZAZJOHTZ+esFwtdmXrmB0Ryg144mqCLInFp9bmDzLdyoiVMeCiQeWgv9U54Sd2q9lgEG/7KcOF5Z2jR1SRJqs0RsSh5w40JrIKtsZ1sPFkEnWgssbQ==";
		try {
			CFCARAClient client = createCFCARAClient();

			CertServiceRequestVO certServiceRequestVO = new CertServiceRequestVO();
			certServiceRequestVO.setTxCode("1201");
			// certServiceRequestVO.setLocale(locale);
			certServiceRequestVO.setDn(dn);
			// certServiceRequestVO.setKeyLength(keyLength);
			// certServiceRequestVO.setDuration(duration);
			// certServiceRequestVO.setEndTime(endTime);
			// certServiceRequestVO.setUseOldKey(useOldKey);
			certServiceRequestVO.setP10(p10);

			CertServiceResponseVO certServiceResponseVO = (CertServiceResponseVO) client.process(certServiceRequestVO);

			System.out.println(certServiceResponseVO.getResultCode());
			System.out.println(certServiceResponseVO.getResultMessage());
			if (CFCARAClient.SUCCESS.equals(certServiceResponseVO.getResultCode())) {
				System.out.println(certServiceResponseVO.getSerialNo());
				System.out.println(certServiceResponseVO.getStartTime());
				System.out.println(certServiceResponseVO.getEndTime());
				System.out.println(certServiceResponseVO.getSignatureCert());
				System.out.println(certServiceResponseVO.getEncryptionCert());
				System.out.println(certServiceResponseVO.getEncryptionPrivateKey());
			}
		} catch (RATKException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 证书查询
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月22日 下午1:27:12
	 */
	public static void queryCer() {
		try {
			CFCARAClient client = createCFCARAClient();

			QueryRequestVO queryRequestVO = new QueryRequestVO();
			queryRequestVO.setTxCode("7101");
			// queryRequestVO.setLocale(locale);
			// queryRequestVO.setCertType(certType);
			// queryRequestVO.setCustomerType(customerType);
			// queryRequestVO.setUserName(userName);
			// queryRequestVO.setUserNameInDn(userNameInDn);
			// queryRequestVO.setUserIdent(userIdent);
			// queryRequestVO.setIdentType(identType);
			// queryRequestVO.setIdentNo(identNo);
			// queryRequestVO.setKeyAlg(keyAlg);
			// queryRequestVO.setKeyLength(keyLength);
			// queryRequestVO.setDn(dn);
			// queryRequestVO.setSerialNo(serialNo);
			// queryRequestVO.setCertStatus(certStatus);
			// queryRequestVO.setBranchCode(branchCode);
			// queryRequestVO.setEmail(email);
			// queryRequestVO.setStartTimeFrom(startTimeFrom);
			// queryRequestVO.setStartTimeTo(startTimeTo);
			// queryRequestVO.setEndTimeFrom(endTimeFrom);
			// queryRequestVO.setEndTimeTo(endTimeTo);
			// queryRequestVO.setExactly(exactly);

			QueryResponseListVO queryResponseListVO = (QueryResponseListVO) client.process(queryRequestVO);

			System.out.println(queryResponseListVO.getResultCode());
			System.out.println(queryResponseListVO.getResultMessage());
			List<QueryResponseVO> queryResponseVOList = queryResponseListVO.getQueryResponseVOList();
			if (CFCARAClient.SUCCESS.equals(queryResponseListVO.getResultCode()) && queryResponseVOList != null && queryResponseVOList.size() > 0) {
				System.out.println(queryResponseVOList.size());

				// 字段值不存在时为null
				QueryResponseVO queryResponseVO = queryResponseVOList.get(0);
				System.out.println(queryResponseVO.getCertType());
				System.out.println(queryResponseVO.getCustomerType());
				System.out.println(queryResponseVO.getUserName());
				System.out.println(queryResponseVO.getUserNameInDn());
				System.out.println(queryResponseVO.getUserIdent());
				System.out.println(queryResponseVO.getIdentType());
				System.out.println(queryResponseVO.getIdentNo());
				System.out.println(queryResponseVO.getDn());
				System.out.println(queryResponseVO.getSequenceNo());
				System.out.println(queryResponseVO.getSerialNo());
				System.out.println(queryResponseVO.getCertStatus());
				System.out.println(queryResponseVO.getDuration());
				System.out.println(queryResponseVO.getApplyTime());
				System.out.println(queryResponseVO.getStartTime());
				System.out.println(queryResponseVO.getEndTime());
				System.out.println(queryResponseVO.getRevokeTime());
				System.out.println(queryResponseVO.getBranchCode());
				System.out.println(queryResponseVO.getKeyAlg());
				System.out.println(queryResponseVO.getKeyLength());
				System.out.println(queryResponseVO.getEmail());
				System.out.println(queryResponseVO.getPhoneNo());
				System.out.println(queryResponseVO.getAddress());
			}
		} catch (RATKException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 唯一证书查询
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月22日 下午1:28:17
	 * @param seriaNo
	 */
	public static void queryCerBySeriaNo(String serialNo) {
		// String locale = "zh_CN";
		// String serialNo = "2000631224";
		// String dn =
		// "cn=05@testName@Z1234567890@1,ou=Individual-1,ou=Local RA,o=CFCA TEST CA,c=cn";
		try {
			CFCARAClient client = createCFCARAClient();

			QueryRequestVO queryRequestVO = new QueryRequestVO();
			queryRequestVO.setTxCode("7102");
			queryRequestVO.setSerialNo(serialNo);
			// queryRequestVO.setDn(dn);

			QueryResponseVO queryResponseVO = (QueryResponseVO) client.process(queryRequestVO);
			// queryRequestVO.setLocale(locale);

			System.out.println(queryResponseVO.getResultCode());
			System.out.println(queryResponseVO.getResultMessage());
			if (CFCARAClient.SUCCESS.equals(queryResponseVO.getResultCode())) {
				System.out.println(queryResponseVO.getCertType());
				System.out.println(queryResponseVO.getCustomerType());
				System.out.println(queryResponseVO.getDn());
				System.out.println(queryResponseVO.getSequenceNo());
				System.out.println(queryResponseVO.getSerialNo());
				System.out.println(queryResponseVO.getCertStatus());
				System.out.println(queryResponseVO.getDuration());
				System.out.println(queryResponseVO.getApplyTime());
				System.out.println(queryResponseVO.getStartTime());
				System.out.println(queryResponseVO.getEndTime());
				System.out.println(queryResponseVO.getBranchCode());
				System.out.println(queryResponseVO.getKeyAlg());
				System.out.println(queryResponseVO.getKeyLength());
				System.out.println(queryResponseVO.getEmail());
			}
		} catch (RATKException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 证书公钥查询
	 * 
	 * @Author zy
	 * @Company: JL
	 * @Create Time: 2014年9月22日 下午1:30:11
	 * @param seriaNo
	 */
	public static void queryCerByCerPublickeyBySeriaNo(String serialNo) {
		// String locale = "zh_CN";
		// String serialNo = "2000631224";
		try {
			CFCARAClient client = createCFCARAClient();

			QueryRequestVO queryRequestVO = new QueryRequestVO();
			queryRequestVO.setTxCode("7103");
			queryRequestVO.setSerialNo(serialNo);

			QueryResponseVO queryResponseVO = (QueryResponseVO) client.process(queryRequestVO);
			// queryRequestVO.setLocale(locale);

			System.out.println(queryResponseVO.getResultCode());
			System.out.println(queryResponseVO.getResultMessage());
			if (CFCARAClient.SUCCESS.equals(queryResponseVO.getResultCode())) {
				System.out.println(queryResponseVO.getSignatureCert());
				System.out.println(queryResponseVO.getEncryptionCert());
			}
		} catch (RATKException e) {
			e.printStackTrace();
		}
	}

}
