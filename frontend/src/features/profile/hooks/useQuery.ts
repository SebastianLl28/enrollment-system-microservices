import { PROFILE_QUERY } from "@/config/keys";
import { useQuery } from "@tanstack/react-query";
import { getProfile } from "../services/profileService";

export const useGetProfile = () =>
  useQuery({
    queryKey: PROFILE_QUERY,
    queryFn: getProfile,
  });
